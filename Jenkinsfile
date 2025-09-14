// 使用声明式流水线语法
pipeline {
    // 定义在Kubernetes集群中运行的agent
    agent {
        kubernetes {
            cloud 'docker-desktop'
            slaveConnectTimeout 1200
            yaml """
apiVersion: v1
kind: Pod
metadata:
  name: jenkins-agent
  namespace: jenkins-agents
spec:
  containers:
  - name: jnlp  # JNLP容器必须作为第一个容器，用于与Jenkins主节点通信
    image: jenkins/inbound-agent:4.23-1-jdk17
    args: ['\$(JENKINS_SECRET)', '\$(JENKINS_NAME)']
    imagePullPolicy: IfNotPresent
    resources:
      requests:
        memory: "256Mi"
        cpu: "250m"
      limits:
        memory: "512Mi"
        cpu: "500m"
    env:
    - name: TZ
      value: "Asia/Shanghai"
  - name: jdk
    image: gradle:7.6-jdk11  # 包含Maven和JDK的镜像
    imagePullPolicy: IfNotPresent
    command: ['cat']
    tty: true
    resources:
      requests:
        memory: "2Gi"
        cpu: "1000m"
      limits:
        memory: "4Gi"
        cpu: "2000m"
    volumeMounts:
      - name: cache-dir
        mountPath: /root/.gradle  # Maven缓存目录
        readOnly: false
    env:
    - name: TZ
      value: "Asia/Shanghai"
  - name: docker
    image: docker:20.10-dind  # Docker-in-Docker用于构建镜像
    command: ['cat']
    imagePullPolicy: IfNotPresent
    tty: true
    securityContext:
      privileged: true  # Docker需要特权模式
    resources:
      requests:
        memory: "1Gi"
        cpu: "500m"
      limits:
        memory: "2Gi"
        cpu: "1000m"
    env:
    - name: TZ
      value: "Asia/Shanghai"
  - name: kubectl
    image: bitnami/kubectl:latest  # 包含kubectl的镜像
    command: ['cat']
    imagePullPolicy: IfNotPresent
    tty: true
    resources:
      requests:
        memory: "512Mi"
        cpu: "250m"
      limits:
        memory: "1Gi"
        cpu: "500m"
    env:
    - name: TZ
      value: "Asia/Shanghai"
  volumes:
  - name: cache-dir
    emptyDir: {}  # 临时存储Maven依赖
  restartPolicy: Never
"""
            // 指定默认容器（用于非container步骤）
            defaultContainer 'jdk'
            // 空闲时自动删除Pod（分钟）
            idleMinutes 10
            // Pod存活时间（分钟）
            activeDeadlineSeconds 3600
        }
    }

    // 环境变量配置
    environment {
        // 应用配置
        APP_NAME = 'backend-scaffold'
        COMMIT_ID = ""
        HARBOR_ADDRESS = "192.168.31.111:8081"
        REGISTRY_DIR = "example"
        IMAGE_NAME = "backend-scaffold"
        NAMESPACE = "applications"
        TAG = ""

        VERSION = "${currentBuild.number}-${env.GIT_COMMIT.substring(0, 7)}"
        DOCKER_IMAGE = "${HARBOR_ADDRESS}/${APP_NAME}:${VERSION}"
    }

    // 参数化构建配置
    parameters {
        gitParameter(
            branch: '',
            branchFilter: 'origin/(.*)',
            defaultValue: '',
            description: 'branch for build and deploy',
            name: 'BRANCH',
            quickFilterEnabled: false,
            selectedValue: 'NONE',
            sortMode: 'NONE', tagFilter: '*',
            type: 'PT_BRANCH'
        )
        choice(
            name: 'DEPLOY_ENV',
            choices: ['dev', 'staging', 'production'],
            description: '选择部署环境'
        )
        string(
            name: 'IMAGE_TAG',
            defaultValue: "${currentBuild.number}",
            description: 'Docker镜像标签'
        )
        booleanParam(
            name: 'RUN_INTEGRATION_TESTS',
            defaultValue: true,
            description: '是否运行集成测试'
        )
    }

    // 选项配置
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 60, unit: 'MINUTES')
        disableConcurrentBuilds()
        // 保存制品和日志用于调试
        preserveStashes(buildCount: 5)
    }

    // 阶段定义
    stages {
        // 阶段1: 代码检出
        stage('Checkout') {
            when {
                expression {
                    env.githubBranch == null
                }
            }
            steps {
                container('jdk') {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${env.BRANCH_NAME ?: 'main'}"]],
                        extensions: [],
                        userRemoteConfigs: [[
                            credentialsId: 'github-macos',
                            url: 'git@github.com:geejay-woo/backend-scaffold.git'
                        ]]
                    ])

                    // 记录Git信息
                    script {
                        env.GIT_COMMIT = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                        env.GIT_BRANCH = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                        echo "Building branch: ${env.GIT_BRANCH}, commit: ${env.GIT_COMMIT}"
                    }
                }
            }
        }

        // 阶段2: 代码编译和单元测试
        stage('Build and Test') {
            steps {
                container('jdk') {
                    // 编译代码
                    sh 'gradle clean build -x test'

                    // 运行单元测试
                    sh 'gradle test'

                    // 生成测试报告
                    junit 'build/test-results/test/*.xml'

                    // 代码覆盖率报告（如果配置了JaCoCo）
                    jacoco()
                }
            }
        }

        // 阶段4: 构建JAR包
        stage('Package') {
            steps {
                container('jdk') {
                    sh 'gradle assemble'

                    // 修改存档路径
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true

                    // 修改存储路径
                    stash name: 'app-jar', includes: 'build/libs/*.jar'
                }
            }
        }

        // 阶段5: 构建Docker镜像
        stage('Build Docker Image') {
            steps {
                container('docker') {
                    // 从之前阶段获取JAR文件
                    unstash 'app-jar'

                    // 构建Docker镜像
                    script {
                        docker.build("${DOCKER_IMAGE}", "--build-arg JAR_FILE=build/libs/*.jar -f Dockerfile .")
                    }
                }
            }
        }

        // 阶段6: 推送Docker镜像
        stage('Push Docker Image') {
            environment {
                HARBOR_USER = credentials('harbor-account')
            }
            steps {
                container('docker') {
                    sh """
                    docker login -u ${HARBOR_USER_USR} -p ${HARBOR_USER_PSW} ${HARBOR_ADDRESS}
                    docker push ${HARBOR_ADDRESS}/${REGISTRY_DIR}/${IMAGE_NAME}:${TAG}
                    """
                }
            }
        }

        // 阶段7: 部署到Kubernetes
        stage('Deploy to Kubernetes') {
            environment {
                MY_KUBECONFIG = credentials('k8s-docker-desktop')
            }
            steps {
                container('kubectl') {
                    sh """
                    /usr/local/bin/kubectl --kubeconfig ${MY_KUBECONFIG} set image deploy -l app=${IMAGE_NAME} ${IMAGE_NAME}=${HARBOR_ADDRESS}/${REGISTRY_DIR}/${IMAGE_NAME}:${TAG} -n ${NAMESPACE}
                    """
                }
            }
        }
    }
}
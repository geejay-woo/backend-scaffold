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
      - name: cacheDir
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
  - name: cacheDir
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

//         DOCKER_REGISTRY = 'registry.example.com'
//
//         // 凭据配置（在Jenkins中预先配置）
//         REGISTRY_CREDENTIALS = credentials('docker-registry-creds')
//         KUBECONFIG_CREDENTIALS = credentials('k8s-cluster-config')
//
//         // Kubernetes配置
//         K8S_NAMESPACE = 'spring-apps'
//         DEPLOYMENT_NAME = "${APP_NAME}-deployment"
//         SERVICE_NAME = "${APP_NAME}-service"
//
//         // 部署策略（可通过参数修改）
//         DEPLOY_STRATEGY = 'rolling-update'
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

//             post {
//                 failure {
//                     // 测试失败时发送通知
//                     slackSend(
//                         channel: '#build-failures',
//                         message: "❌ 构建失败: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n测试阶段失败 - ${env.BUILD_URL}"
//                     )
//                 }
//             }
        }

        // 阶段3: 代码质量检查
//         stage('Code Quality') {
//             steps {
//                 container('jdk') {
//                     // 使用SonarQube进行静态代码分析
//                     withSonarQubeEnv('sonar-server') {
//                         sh 'mvn sonar:sonar -Dsonar.projectVersion=${VERSION}'
//                     }
//                 }
//             }
//         }

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
//                     script {
//                         // 登录到Docker注册表
//                         sh "echo '${REGISTRY_CREDENTIALS_PSW}' | docker login -u '${REGISTRY_CREDENTIALS_USR}' --password-stdin ${DOCKER_REGISTRY}"
//
//                         // 推送镜像
//                         sh "docker push ${DOCKER_IMAGE}"
//
//                         // 如果是main分支，同时推送latest标签
//                         if (env.GIT_BRANCH == 'main') {
//                             sh "docker tag ${DOCKER_IMAGE} ${DOCKER_REGISTRY}/${APP_NAME}:latest"
//                             sh "docker push ${DOCKER_REGISTRY}/${APP_NAME}:latest"
//                         }
//                     }
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
//                     script {
//                         // 写入kubeconfig文件
//                         writeFile file: 'kubeconfig.yaml', text: "${KUBECONFIG_CREDENTIALS}"
//
//                         // 部署到Kubernetes
//                         sh """
//                             kubectl --kubeconfig=kubeconfig.yaml apply -f k8s/namespace.yaml || true
//                             cat k8s/deployment.yaml | sed 's|IMAGE_PLACEHOLDER|${DOCKER_IMAGE}|g' | kubectl --kubeconfig=kubeconfig.yaml apply -n ${K8S_NAMESPACE} -f -
//                             kubectl --kubeconfig=kubeconfig.yaml apply -n ${K8S_NAMESPACE} -f k8s/service.yaml
//                             kubectl --kubeconfig=kubeconfig.yaml apply -n ${K8S_NAMESPACE} -f k8s/ingress.yaml
//                         """
//
//                         // 等待部署完成
//                         sh """
//                             kubectl --kubeconfig=kubeconfig.yaml rollout status deployment/${DEPLOYMENT_NAME} -n ${K8S_NAMESPACE} --timeout=300s
//                         """
//
//                         // 获取服务信息
//                         def serviceInfo = sh(returnStdout: true, script: "kubectl --kubeconfig=kubeconfig.yaml get svc ${SERVICE_NAME} -n ${K8S_NAMESPACE} -o jsonpath='{.status.loadBalancer.ingress[0].ip}'").trim()
//                         echo "应用已部署，访问地址: http://${serviceInfo}"
//                     }
                }
            }
        }

//         // 阶段8: 集成测试（可选）
//         stage('Integration Tests') {
//             when {
//                 expression { return params.RUN_INTEGRATION_TESTS.toBoolean() }
//             }
//             steps {
//                 container('jdk') {
//                     // 运行集成测试
//                     sh 'mvn verify -DskipUnitTests'
//
//                     // 生成集成测试报告
//                     junit 'target/failsafe-reports/*.xml'
//                 }
//             }
//         }
//
//         // 阶段9: 健康检查
//         stage('Health Check') {
//             steps {
//                 container('kubectl') {
//                     script {
//                         // 等待应用启动并健康
//                         sh """
//                             # 健康检查脚本
//                             attempts=0
//                             max_attempts=30
//                             while [ \$attempts -lt \$max_attempts ]; do
//                                 response=\$(curl -s -o /dev/null -w "%{http_code}" http://${SERVICE_NAME}.${K8S_NAMESPACE}.svc.cluster.local:8080/actuator/health || true)
//                                 if [ "\$response" = "200" ]; then
//                                     echo "应用健康检查通过"
//                                     break
//                                 fi
//                                 echo "等待应用启动... (\$((attempts+1))/\$max_attempts)"
//                                 sleep 10
//                                 attempts=\$((attempts+1))
//                             done
//                             if [ \$attempts -eq \$max_attempts ]; then
//                                 echo "应用健康检查失败"
//                                 exit 1
//                             fi
//                         """
//                     }
//                 }
//             }
//         }
    }

    // 构建后处理
//     post {
//         // 构建成功
//         success {
//             script {
//                 // 发送成功通知
//                 slackSend(
//                     channel: '#deployments',
//                     message: "✅ 部署成功: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n环境: ${params.DEPLOY_ENV}\n镜像: ${DOCKER_IMAGE}\n详情: ${env.BUILD_URL}"
//                 )
//
//                 // 邮件通知
//                 emailext(
//                     subject: "SUCCESS: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
//                     body: """
//                         构建成功!
//                         项目: ${env.JOB_NAME}
//                         构建编号: ${env.BUILD_NUMBER}
//                         环境: ${params.DEPLOY_ENV}
//                         镜像: ${DOCKER_IMAGE}
//                         提交: ${env.GIT_COMMIT}
//                         详情: ${env.BUILD_URL}
//                     """,
//                     to: 'devops-team@example.com'
//                 )
//             }
//         }
//
//         // 构建失败
//         failure {
//             script {
//                 // 发送失败通知
//                 slackSend(
//                     channel: '#build-failures',
//                     message: "❌ 部署失败: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n环境: ${params.DEPLOY_ENV}\n详情: ${env.BUILD_URL}"
//                 )
//
//                 // 收集诊断信息
//                 container('kubectl') {
//                     sh """
//                         kubectl --kubeconfig=kubeconfig.yaml describe pod -l app=${APP_NAME} -n ${K8S_NAMESPACE} || true
//                         kubectl --kubeconfig=kubeconfig.yaml logs -l app=${APP_NAME} -n ${K8S_NAMESPACE} --tail=50 || true
//                     """
//                 }
//             }
//         }

        // 总是执行的操作
        always {
            // 清理工作空间
            cleanWs()

            // 记录构建指标
            recordIssues(
                tools: [java(), maven()],
                enabledForFailure: true
            )

            // 更新构建描述
            script {
                currentBuild.description = "Env: ${params.DEPLOY_ENV}, Image: ${DOCKER_IMAGE}"
            }
        }
    }
}
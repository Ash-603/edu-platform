# edu-platform
# 在线教育平台前端项目

## 项目概述

这是一个现代化的在线教育平台前端项目，使用 Vue.js 和 Vuetify 构建。该平台为学生和教师提供了全面的在线学习和教学体验，包括课程管理、视频课程、作业评分、实时聊天等功能。

## 主要功能

### 用户角色

- **学生**：可以浏览课程、注册学习、提交作业、获取证书
- **教师**：可以创建课程、管理内容、批改作业、分析学生数据

### 核心功能

1. **用户管理**
   - 注册/登录（包括社交媒体登录）
   - 用户档案管理
   - 角色权限控制

2. **课程系统**
   - 课程浏览与搜索
   - 课程创建与编辑
   - 多媒体学习材料（视频、文档、测验）
   - 进度跟踪

3. **作业系统**
   - 作业提交界面
   - 教师评分工具
   - 评分标准和反馈机制

4. **数据分析**
   - 学生学习进度分析
   - 教师课程效果分析
   - 成绩统计和可视化

5. **通信系统**
   - 课程讨论区
   - 私人消息
   - 公告通知

## 技术栈

- **前端框架**：Vue.js
- **UI 库**：Vuetify (基于 Material Design)
- **状态管理**：Vuex
- **路由**：Vue Router
- **HTTP 客户端**：Axios
- **图表**：Chart.js
- **日期处理**：Moment.js

## 项目结构

```
src/
├── assets/           # 静态资源
├── components/       # 共享组件
├── plugins/          # Vue 插件配置
├── router/           # 路由配置
├── store/            # Vuex 状态管理
├── styles/           # 全局样式
├── utils/            # 工具函数
└── views/            # 页面组件
    ├── auth/         # 认证相关页面
    ├── course/       # 课程相关页面
    ├── dashboard/    # 用户仪表盘
    ├── assignment/   # 作业相关页面
    └── teacher/      # 教师专用页面
```

## 主要页面

1. **登录注册页面** (Login.vue)
   - 用户认证界面，支持多种登录方式

2. **用户仪表盘** (UserDashboard.vue)
   - 学生课程概览
   - 学习进度
   - 待完成作业
   - 获得的证书

3. **教师仪表盘** (TeacherDashboard.vue)
   - 教授的课程
   - 学生统计
   - 收入分析
   - 待评分作业

4. **课程创建页面** (CourseCreate.vue)
   - 课程基本信息
   - 课程内容管理
   - 价格和访问设置
   - 课程评审和发布

5. **作业提交页面** (AssignmentSubmission.vue)
   - 作业要求查看
   - 文本和文件提交
   - 评分和反馈查看

6. **作业评分页面** (GradeAssignment.vue)
   - 学生提交查看
   - 评分工具
   - 班级进度统计
   - 批量评分功能

## 安装指南

1. 克隆仓库
```bash
git clone https://github.com/Ash-603/edu-platform.git
cd online-education-platform
```

2. 安装依赖
```bash
npm install
```

3. 运行开发服务器
```bash
npm run serve
```

4. 构建生产版本
```bash
npm run build
```

## 环境变量配置

创建 `.env` 文件并配置以下环境变量：

```
VUE_APP_API_URL=https://api.yourdomain.com
VUE_APP_STORAGE_URL=https://storage.yourdomain.com
VUE_APP_GOOGLE_CLIENT_ID=your-google-client-id
```

## API 集成

本项目设计为与后端 API 配合使用。主要的 API 端点包括：

- 用户认证: `/api/auth/*`
- 课程管理: `/api/courses/*`
- 作业系统: `/api/assignments/*`
- 聊天系统: `/api/chat/*`

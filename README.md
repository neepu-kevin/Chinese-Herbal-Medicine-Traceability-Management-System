<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-d3d0a9303e11d522a06cd263f3079027715.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi v3.9.2</h1>
<h4 align="center">基于SpringBoot+Vue前后端分离的Java快速开发框架</h4>
<p align="center">
	<a href="https://gitee.com/y_project/RuoYi-Vue/stargazers"><img src="https://gitee.com/y_project/RuoYi-Vue/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/y_project/RuoYi-Vue"><img src="https://img.shields.io/badge/RuoYi-v3.9.2-brightgreen.svg"></a>
	<a href="https://gitee.com/y_project/RuoYi-Vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 平台简介

若依是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 前端采用Vue、Element UI。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。
* 权限认证使用Jwt，支持多终端认证系统。
* 支持加载动态权限菜单，多方式轻松权限控制。
* 高效率开发，使用代码生成器可以一键生成前后端代码。
* 阿里云折扣场：[点我进入](http://aly.ruoyi.vip)，腾讯云秒杀场：[点我进入](http://txy.ruoyi.vip)&nbsp;&nbsp;

# 版本分支

RuoYi-Vue 后端项目提供 Spring Boot 2.x / 3.x / 4.x 多版本分支的并行维护。

| 名称              | 说明                      | 地址                                                    |
| :---------------- | :------------------------ | :------------------------------------------------------ |
| master 默认分支   | Spring Boot 4.x (JDK 17+) | https://gitee.com/y_project/RuoYi-Vue                   |
| springboot3 分支  | Spring Boot 3.x (JDK 17+) | https://gitee.com/y_project/RuoYi-Vue/tree/springboot3  |
| springboot2 分支  | Spring Boot 2.x (JDK 8+)  | https://gitee.com/y_project/RuoYi-Vue/tree/springboot2  |  

RuoYi-Vue 前端项目提供 Vue 2.x / 3.x / JavaScript TypeScript 版本均可混用搭配

| 项目名称      | **RuoYi-Vue** | **RuoYi-Vue3** | **RuoYi-Vue3-TypeScript**   |
| :---          | :---          | :---           | :---                        |
| **前端框架**  | Vue 2        | Vue 3          | Vue 3                       |
| **脚本语言**  | JavaScript   | JavaScript     | TypeScript                  |
| **构建工具**  | Vue CLI      | Vite           | Vite                        |
| **UI 组件库** | Element UI   | Element Plus   | Element Plus                |
| **状态管理**  | Vuex         | Pinia          | Pinia                       |
| **路由管理**  | Vue Router 3 | Vue Router 4   | Vue Router 4                |
| **核心特点**  | 1. 技术栈经典稳定<br>2. 社区资料丰富<br>3. 当前维护重心已转移 | 1. 现代前端技术栈<br>2. 开发体验与性能更优<br>3. 官方主推的活跃版本 | 1. 类型加持，减少沟通成本<br>2. 开发时有提示，效率更高<br>3. 多人协作企业级开发项目 |
| **仓库地址**  | [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) | [RuoYi-Vue3](https://gitcode.com/yangzongzhuan/RuoYi-Vue3) | [RuoYi-Vue3-TypeScript](https://gitcode.com/yangzongzhuan/RuoYi-Vue3/tree/typescript) |

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的HTML代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。


# 中草药溯源管理系统（TCM Traceability System）

## 1. 项目简介

本项目是一个基于 Vue + Spring Boot + MySQL 的中草药溯源管理系统，实现对中草药从 **种植 → 加工 → 仓储 → 检测 → 销售** 全生命周期的数据记录与追溯。

系统支持多角色协同操作，并引入 XGBoost 模型进行质量预测，提高系统智能化水平。

---

## 2. 技术栈

### 前端
- Vue 3
- Element Plus
- Axios

### 后端
- Spring Boot
- Spring MVC
- MyBatis / JPA

### 数据库
- MySQL

### AI模块
- Python
- XGBoost
- REST API

---

## 3. 系统功能模块

### 3.1 用户模块
- 用户注册 / 登录
- 角色权限控制（RBAC）
- 企业信息管理

---

### 3.2 溯源管理模块（核心）
- 生成唯一溯源码（TraceID）
- 绑定产品全生命周期数据
- 支持二维码查询

---

### 3.3 业务数据模块

#### 种植管理
- 地块管理
- 环境数据记录（温度、湿度等）
- 农药使用记录

#### 加工管理
- 加工方式记录
- 批次管理

#### 仓储管理
- 入库 / 出库管理
- 存储环境监控

#### 销售管理
- 产品上架
- 销售记录

---

### 3.4 检测模块
- 检测报告上传
- 农残、重金属指标记录
- 合格判定

---

### 3.5 查询模块
- 输入溯源码查询
- 展示全流程数据（时间线）

---

### 3.6 AI预测模块（XGBoost）

#### 功能
预测中草药是否合格

#### 输入特征
- 土壤湿度
- 温度
- 农药使用量
- 光照
- 降水

#### 输出
- 合格 / 不合格

---

## 4. 系统角色设计

| 角色 | 功能 |
|------|------|
| 生产者 | 记录种植数据 |
| 加工企业 | 记录加工数据 |
| 仓储企业 | 记录仓储数据 |
| 质检机构 | 上传检测报告 |
| 销售企业 | 管理销售 |
| 消费者 | 查询溯源 |
| 政府监管者 | 数据监管 |
| 管理员 | 系统管理 |

---

## 5. 数据库设计

### 5.1 设计思想

系统采用 TraceID 作为核心主键，贯穿所有业务表：

TraceID → 全流程数据

---

### 5.2 数据表结构

#### 5.2.1 用户表 user

```sql
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(20),
    company_name VARCHAR(100),
    create_time DATETIME
);
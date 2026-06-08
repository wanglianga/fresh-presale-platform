# 生鲜预售平台

社区团购全链路管理系统，覆盖消费者、团长、供应商、仓配团队四个角色，实现预售 → 采购 → 到仓 → 分拣 → 团点签收 → 自提 → 退款补偿的完整业务闭环。

## 原始需求

> 搭建一个给社区团购团长、供应商、仓配团队和消费者使用的生鲜预售平台，Vue3 页面呈现团购商品、团长订单、分拣差异和售后处理，Spring Boot 保存预售单、采购批次、到货分拣和退款记录。消费者按小区下单蔬果、肉禽、冷冻品和组合套餐；供应商确认采购量、发货批次和保鲜条件；仓配团队分拣到小区团点，记录缺货、破损、重量差异和替换商品；团长核对到货、通知自提和处理售后。系统要把预售、采购、到仓、分拣、团点签收、自提和退款补偿连成闭环。缺货、重量不足、冷链破损、团长漏发要分别影响履约和赔付。

## 技术栈

### 前端
- Vue 3 + Vite
- Element Plus UI 组件库
- Vue Router 路由
- Pinia 状态管理
- Axios HTTP 请求

### 后端
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL 8.0
- Lombok
- SpringDoc OpenAPI (Swagger)

### 部署
- Docker Compose

## 功能模块

| 模块 | 说明 |
|------|------|
| 数据概览 | 订单统计、差异分析、业务流程可视化 |
| 团购商品 | 商品管理（蔬果/肉禽/冷冻品/组合套餐）、分类、库存、保鲜条件 |
| 团长订单 | 预售单管理、状态流转、商品明细、自提码 |
| 采购批次 | 采购计划、供应商确认、发货、到货管理、保鲜条件 |
| 分拣差异 | 到仓分拣、缺货/重量差异/冷链破损/替换商品记录、赔付处理 |
| 售后处理 | 售后申请审批、退款记录、补偿记录 |
| 基础配置 | 预售批次、供应商、团长、小区团点、团长签收与自提通知 |

## 异常场景与赔付规则

| 异常类型 | 说明 | 履约影响 | 赔付方式 |
|----------|------|----------|----------|
| 缺货 | 供货商未足量供货 | 影响履约率 | 全额退款 |
| 重量不足 | 实际称重少于预售量 | 影响履约率 | 差额退款 |
| 冷链破损 | 冷链运输中温度异常导致变质 | 影响履约率 | 全额退款 + 补偿 |
| 团长漏发 | 团长未将商品交付消费者 | 影响履约率 | 补发或补偿 |
| 替换商品 | 缺货时使用同价位商品替换 | 部分影响 | 记录并通知 |

## 启动方式

### 方式一：Docker 一键启动（推荐）

#### 前置要求

- Docker 20.10+
- Docker Compose 2.0+

#### 启动步骤

#### 1. 一键构建并启动

```bash
docker compose up --build
```

如需后台运行：

```bash
docker compose up --build -d
```

#### 2. 访问地址

- 前端页面：http://localhost:80
- 后端 API：http://localhost:8080/api
- Swagger 文档：http://localhost:8080/api/swagger-ui.html
- MySQL：localhost:3306（用户名：root，密码：password，数据库：fresh_presale）

#### 3. 停止和清理

```bash
docker compose down
```

如需清除数据库数据：

```bash
docker compose down -v
```

---

### 方式二：本地开发启动

#### 前置要求

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+

#### 启动步骤

#### 1. 初始化数据库

创建数据库和用户（可选，默认使用 root）：

```sql
CREATE DATABASE fresh_presale DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改后端配置文件 `backend/src/main/resources/application.yml` 中的数据库连接信息，或设置环境变量：

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=fresh_presale
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

#### 2. 启动后端服务

```bash
cd backend
mvn clean spring-boot:run
```

后端启动后访问：
- API 地址：http://localhost:8080/api
- Swagger 文档：http://localhost:8080/api/swagger-ui.html

#### 3. 启动前端服务

新开一个终端窗口：

```bash
cd frontend
npm install
npm run dev
```

前端启动后访问：http://localhost:5173

前端开发模式下已配置代理，会自动将 `/api` 请求转发到 `http://localhost:8080`。

## 项目目录结构

```
wmy-40/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/fresh/
│   │   ├── entity/            # 数据库实体
│   │   ├── repository/        # JPA Repository
│   │   ├── service/           # 业务服务层
│   │   ├── controller/        # REST Controller
│   │   ├── common/            # 通用类（Result、异常处理）
│   │   ├── config/            # 配置类
│   │   └── FreshPresaleApplication.java
│   ├── src/main/resources/
│   │   └── application.yml    # 应用配置
│   ├── Dockerfile             # 后端 Dockerfile
│   ├── .dockerignore
│   └── pom.xml
├── frontend/                   # Vue3 前端
│   ├── src/
│   │   ├── api/               # API 接口封装
│   │   ├── router/            # 路由配置
│   │   ├── utils/             # 工具类
│   │   ├── views/             # 页面组件
│   │   ├── App.vue
│   │   ├── main.js
│   │   └── style.css
│   ├── Dockerfile             # 前端 Dockerfile (含 Nginx)
│   ├── nginx.conf             # Nginx 配置
│   ├── .dockerignore
│   ├── vite.config.js
│   ├── package.json
│   └── index.html
├── Dockerfile                 # 根目录 Dockerfile（后端构建入口）
├── docker-compose.yml         # Docker Compose 编排
├── .dockerignore
├── .done                      # 任务执行过程记录
└── README.md
```

## API 接口概览

| 模块 | 接口前缀 | 说明 |
|------|----------|------|
| 商品 | `/api/products` | 商品 CRUD |
| 订单 | `/api/orders` | 预售单管理、状态更新 |
| 采购 | `/api/purchases` | 采购批次创建、确认、发货、到货 |
| 分拣 | `/api/sorting/records` | 分拣记录、分拣完成 |
| 分拣差异 | `/api/sorting/discrepancies` | 差异记录、差异处理 |
| 售后申请 | `/api/aftersale/requests` | 售后申请、审批通过/拒绝 |
| 退款 | `/api/aftersale/refunds` | 退款记录、确认退款 |
| 补偿 | `/api/aftersale/compensations` | 补偿记录、确认补偿 |
| 基础配置 | `/api/basic/*` | 预售批次、供应商、团长、小区、签收自提 |

详细接口文档请启动后访问 Swagger UI。

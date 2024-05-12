# SecuriVault

> 守护秘匣 - 基于 Springboot 的跨端密码管理平台
> 
> 后端平台：https://github.com/XiaoLFeng/SecuriVault
> 前端平台：https://github.com/XiaoLFeng/securi-vault-web

## 项目介绍

### 核心特性

- **自有化部署**：SecuriVault让用户可以在自己的服务器上部署密码管理平台，确保数据存储的独立性和安全性。
- **跨平台兼容性**：基于SpringBoot的设计让SecuriVault可以轻松地跨多个平台工作，毕竟提供了服务端，用户只需要在web页面等操作就可以了。
- **用户友好的界面**：我们设计了直观的用户界面，使得账户信息和密钥的存储、检索和管理既简单又高效。
- **高级安全措施**：采用加密技术保护用户数据，确保即使数据被不当访问，也无法被轻易解密。

### 为什么选择SecuriVault？

- **数据主权**：与依赖第三方云服务不同，SecuriVault的自有化部署模式让数据的控制权完全回到用户手中，极大地降低了数据泄露的风险。
- **透明可靠**：开发者和用户可以更加透明地了解数据如何被存储和处理，无需担心背后可能存在的数据使用和分析活动。
- **灵活性和可扩展性**：基于Spring Boot的架构不仅保证了高度的灵活性，也使得SecuriVault能够随着您的业务或个人需求成长而扩展。

## 其他说明

本项目仅为提供一个密码管理平台，平台尽可能保证用户密码的安全性。若用户自身操作不当导致数据丢失以及泄漏等问题，开发者对此概不负责。

平台采用前后端分离方式进行开发（实际上我不太喜欢使用 SpringBoot 使用 Thymeleaf 框架进行前端构建开发，写出来的代码总觉得还是很奇怪，所以采用前后端分离的方式进行开发）。后期其实有打算转为 Golang 语言进行开发的。毕竟这个项目我打算先成为我的大学里面的作业先完成，若成果不错，我自己用的也不错我就转为 Golang 进行开发。【相比 Springboot，对于这种个人项目以及轻量型项目，Golang 也许是一个更好的选择，而且适用性也强】
# Twitter-Clone

## Table of Contents

1. [Team](#team)
2. [Overview](#overview)
    - [Demonstration of the application](#demonstration-of-the-application)
3. [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Run the application](#run-the-application)
    - [Stop the application](#stop-the-application)
4. [Documentation](#documentation)
    - [Sample users](#sample-users)
    - [API REST](#api-rest)
    - [User permissions](#user-permissions)
    - [Functional Requirements](#functional-requirements)
    - [Non Functional Requirements](#non-functional-requirements)
    - [Diagrams](#diagrams)

## Team

| Name                                     | Nickname                                           |
| :--------------------------------------: | :------------------------------------------------: |
| Sergio de Oro Fernández                  | [@keyland71](https://github.com/keyland71)         |
| Laura María Camino Yuste                 | [@Camyus027](https://github.com/Camyus027)         |
| Antonio José Alanís Bernal               | [@antonioalanxs](https://github.com/antonioalanxs) |
| Ikár Martínez de Lizarduy Kostornichenko | [@KandV008](https://github.com/KandV008)           |
| Miguel Ángel Sánchez Miranda             | [@MiguelAngelSM](https://github.com/MiguelAngelSM) |

To organize the team, we use [Trello](https://trello.com/b/WpuV8KtM/la-puta-mejor-práctica-de-daw).

## Overview

Entire Twitter / X Responsive Clone built using [Angular](https://angular.io/), [Tailwind CSS](https://tailwindcss.com/), [Spring Boot](https://spring.io/projects/spring-boot), [MySQL](https://www.mysql.com/) and [Docker](https://www.docker.com/) for educational purposes.

### Demonstration of the application

[![Twitter Clone Demonstration](https://img.youtube.com/vi/R_9iDCZJHPM/maxresdefault.jpg)](https://youtu.be/R_9iDCZJHPM)

## Installation

### Prerequisites

- Have [Docker](https://docs.docker.com/engine/install) installed on your machine.

- Have a [Docker Hub](https://hub.docker.com/) account.

### Cloning the repository

```shell
git clone https://github.com/antonioalanxs/Twitter-Clone
cd Twitter-Clone
```

### Run the application

- Go to the docker directory.

    ```shell
    cd Twitter-Clone/docker
    ```

- Run [build_and_push_image.sh](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docker/build_and_push_image.sh) setting your [Docker Hub](https://hub.docker.com/) username, the name of the image and its version.

    ```shell
    ./build_and_push_image.sh antonioalanxs/Twitter-Clone:0.0.1
    ```

- Finally, execute [run_image.sh](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docker/run_image.sh).

    ```shell
    ./run_image.sh
    ```

    The **Single-page application is now running on your [localhost:8443/new](https://localhost:8443/new)**. Additionally, a web with server-generated [HTML](https://html.spec.whatwg.org/) and [AJAX](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Client-side_web_APIs/Fetching_data) is running on your [localhost:8443](https://localhost:8443/).

### Stop the application

```shell
docker compose down
```

The application was turned off.

## Documentation

### Sample users

| User               | Username          | Password             |
| :----------------: | :----------------:| :------------------: |
|   @admin           |   admin           |   adminpass          |
|   @user            |   user            |   pass               |
|   @antonioalanxs   |   antonioalanxs   |   examplePassword3   |
|   @Keyland71       |   Keyland71       |   examplePassword2   |
|   @ibai            |   ibai            |   ibai               |
|   @Rubiu5          |   Rubiu5          |   rubius             |

### API REST

[Click here](https://rawcdn.githack.com/CodeURJC-DAW-2022-23/webapp8/59faf5ae9cb5cd1cef903fc8699f87c77910a045/api-docs/api-docs.html) to see the API REST documentation. Powered by [Swagger](https://swagger.io/).

### User permissions

- **Anonymous**: User which has not signed up in the application but still has access to a few functionalities.
- **Registered**: User who has already signed up in the application and has a wide variety of available functionalities.
- **Admin**: User who has the overall control of the platform, having the strongest set of permissions.

| User           | Tweets                                        | People                                  | Hashtags                                        | Notifications |
| :------------: | :-------------------------------------------: | :-------------------------------------: | :---------------------------------------------: | :-----------: |
| **Anonymous**  | Read                                          | Read (see their profile)                 | Read (can search them and read the impressions) | Nothing       |
| **Admin**      | Read, write (their own), delete (every tweet) | Read, delete, write (their own profile) | Read, write                                     | Read          |
| **Registered** | Read, write (their own), delete (their own)   | Read, write (their own profile)         | Read, write                                     | Read          |

| Permissions  | Affects to                                                                                              |
| ------------ | ------------------------------------------------------------------------------------------------------- |
| **Images**   | All registered users can upload images                                                                  |
| **Graphics** | Admin users can consult graphics with the statistics of the web (Number of new registered users)        |

### Functional Requirements

| Functional Requirements                                                | Anonymous User     | Registered User    | Admin User         |
| :---------------------------------------------------------: | :----------------: | :----------------: | :----------------: |
| Sign up                                                     | :heavy_check_mark: |                    |                    |
| Log in                                                      |                    | :heavy_check_mark: | :heavy_check_mark: |
| See the preview                                             | :heavy_check_mark: |                    |                    |
| Search (using filters)                                      | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| See tweet information                                       | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Interact with a tweet (retweet, like and comment)           |                    | :heavy_check_mark: | :heavy_check_mark: |
| Reply to a tweet                                            |                    | :heavy_check_mark: | :heavy_check_mark: |
| Save a tweet                                                |                    | :heavy_check_mark: | :heavy_check_mark: |
| Write a tweet                                               |                    | :heavy_check_mark: | :heavy_check_mark: |
| Delete a tweet                                              |                    | :heavy_check_mark: | :heavy_check_mark: |
| Visualize recommend users                                   | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Receive notifications                                       |                    | :heavy_check_mark: | :heavy_check_mark: |
| Receive email notifications                                 | :heavy_check_mark: |                    |                    |
| Visualize statistics                                        |                    |                    | :heavy_check_mark: |
| Edit profile                                                |                    | :heavy_check_mark: | :heavy_check_mark: |
| Visualize profile                                           | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Follow a profile                                            |                    | :heavy_check_mark: | :heavy_check_mark: |
| Visualize tendencies                                        | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Ban users                                                   |                    |                    | :heavy_check_mark: |
| Unbanned users                                              |                    |                    | :heavy_check_mark: |
| Delete tweets                                               |                    |                    | :heavy_check_mark: |
| Verificate users                                            |                    |                    | :heavy_check_mark: |
| Unverificate user                                           |                    |                    | :heavy_check_mark: |

### Non Functional Requirements

- **Responsive Interface**: Ensured optimal viewing and interaction across various devices, enhancing accessibility.

- **User-Friendly Interface**: Ensure a seamless and enjoyable user experience, promoting easy navigation and interaction within the application.

- **Scalability:** Built to handle a growing user base and data efficiently.

- **Availability:** Ensures the application is available and accessible to users when needed.

- **Data should be durable with a SQL Database**.

- **The size of a tweet is 240 characters at most**.

- **Supports tweets comprising text and pictures**.

### Diagrams

| Category                                 | URI                                                                                     |
| :--------------------------------------: | :---------------------------------------------------------------------------------------: |
| [UC1] - Sign up and Log in               | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/useCase1.png)        |
| [UC2] - Admin permissions                | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/useCase2.png)        |
| [UC3] - General permissions              | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/useCase3.png)        |
| [UC4] - Registered permissions           | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/useCase4.png)        |
| [UC5] - User actions                     | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/useCase5.png)        |
| [NM1] - General                          | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/mapNavi1.png)        |
| [NM2] - Anonymous user                   | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/mapNavi2.png)        |
| [NM3] - Administrator user               | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/mapNavi3.png)        |
| [NM4] - Sign up and Password recovery    | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/mapNavi4.png)        |
| Entity - Relationship                    | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/E-R_Diagram.png)     |
| Relational Model                         | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/DB_diagram.png)      |
| Classes                                  | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/Templates.png)       |
| Angular Components and Templates         | [↗️](https://github.com/antonioalanxs/Twitter-Clone/blob/main/docs/SPA_diagram.png)     |


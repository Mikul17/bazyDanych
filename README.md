<a name="readme-top"></a>



<!-- PROJECT LOGO -->
<br />
<div align="center">

<h3 align="center">Betting application</h3>

  <p align="center">
    Simple football betting application

</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Our Fullstack Betting Application allows users to place bets on various match statistics, such as predicting the match winner, total goals, specific team stats and many more ! Odds are generated automatically, and matches are simulated on the backend. The application also features a league table, providing a comprehensive betting experience.


### Built With

* [![Next][Next.js]][Next-url]
* [![React][React.js]][React-url]
* [![Spring-boot][Spring-boot]][React-url]
* [![Material-ui][Material-ui]][Material-url]




<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

To run our application you will need to download follwing tools
### npm
  ```sh
  npm install npm@latest -g
  ```

### MySQL

Can be downloaded directly from <a href="https://dev.mysql.com/downloads/mysql/">Mysql official website </a>
or by running following code:
To download mysql server
```sh
sudo apt install mysql-server
```
To start mysql service in background
```sh
sudo service mysql start
```

### Java
* Windows
1. Go to the [Oracle JDK Downloads](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) page.
2. Download the installer for your operating system.
3. Run the downloaded installer and follow the setup instructions.
* Linux

```sh
sudo apt install openjdk-21-jdk
```

* MacOS
```sh
brew install openjdk@21
```
* Verify the installation by running followin script 
```sh
java -version
```

### Smtp server
I recomand downloading <a href="https://mailpit.axllent.org/docs/install/">Mailpit</a> but you can use smpt server of your choice.

### Installation


1. Clone this repository 
   ```sh
   git clone https://github.com/Mikul17/bazyDanych.git
   ```
2. Create schema in your MySQL database with name of your choice
3. <a href="https://www.devglan.com/online-tools/hmac-sha256-online"> Generate </a> SHA HMAC256 private key for spring boot security
4. Setup required environmental variables listed below :
    * Related to your database : MYSQL_HOST, MYSQL_PORT, MYSQL_USERNAME, MYSQL_PASSWORD, DB_NAME
    * Related to smtp server: SMTP_PORT, SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD
    * Related to spring security: SECRET_KEY = key generated in step 3
5. Open backend folder in your IDE of choice and run project from GUI or by entering following command
   ```sh
   mvn spring-boot:run
   ```
    After successfully running application for first time you can stop it

6. Run script run_sql_scripts.sh located inside SQL folder to populate generated tables with sample data
    ```
    src/main/resources/SQL
    ```
    by entering following command in your terminal, ensure that you are in correct directory
    ```
    ./run_sql_scripts.sh
    ```
7. Run spring application again as instructed in step 5
8. Open frontend project and run following command to install all required packages
    ```
    npm install
    ```
9. After successfully downloading all required packages run frontend application by following command
    ```
    npm run dev
    ```
10. Start Mailpit smtp server by running the exec file or by following command in directory where mailpit is installed
    ```sh
    mailpit
    ```



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Spring-boot]: https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot/
[Material-ui]: https://img.shields.io/badge/Material%20UI-007FFF?style=for-the-badge&logo=mui&logoColor=white
[Material-url]: https://mui.com/material-ui/
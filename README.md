# Codigotón - Bancolombia

_Reto Java propuesto en la codigotón Bancolombia 2021_

## Comenzando 🚀

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu
máquina local para propósitos de desarrollo y pruebas._

### Pre-requisitos ⭐

- Tener el jdk 1.8
- Tener maven instalado
- Tener una instancia con la base de datos mariadb en el puerto 3306

### Instalación 🔧

_Paso a paso para ejecutar el proyecto desde tu computadora_

_Clona el repositorio_

```
git clone https://github.com/dmedinao11/codigoton.git
```

_Abre la carpeta y compila la aplicación (maven es necesario)_

```
cd codigoton
mvn clean compile install package
```

_Ejecuta la aplicación_

```
java -jar target/codigoton-0.0.1-SNAPSHOT.jar
```

_La aplicación tomará como entrada por defecto al archivo input.txt, para cambiar el
arhivo agrega el argumento --input=myfile.txt, como la ruta relativa_

```
java -jar target/codigoton-0.0.1-SNAPSHOT.jar --input=myfile.txt
```

## Construido con 🛠️

- [Java](https://www.java.com/) - Lenguaje de programación
- [Maven](https://maven.apache.org) - Administrador de proyecto y dependencias
- [SpringBoot](https://spring.io/projects/spring-boot) - Framework de java para la
  creación de aplicaciones en Spring
- [Spring Data JDBC](https://maven.apache.org) - Módulo para tratar la conexión JDBC con
  la base de datos
- [Lombok](https://projectlombok.org/) - Herramienta de desarrollo para la generación de
  código mediante anotaciones
- [MariaDB](https://mariadb.org/) - Base de datos, usar con la versión 10.4.13
- [Apache HttpComponents](https://hc.apache.org/) - Biblioteca que permite realizar
  peticiones Http

## Autor ✒️

- **Daniel Medina** - _Desarrollo_ - [dmedinao11](https://github.com/dmedinao11)

---

⌨️ con ❤️ por [dmedinao11](https://github.com/dmedinao11) 😊

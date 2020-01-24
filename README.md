# Clustering

Clustering is project to have only one program running in a machine, and when that machine is down they have capability to run other machine.

# Usage

To start the clustering, first create the database or use an already created
(Default).
`spring.datasource.url = jdbc:postgresql://localhost:5432/processmanager
spring.datasource.username=postgres
spring.datasource.password=postgres`

# Properties

Properties setted on **application.properties**.
- computer.priority-top (Default: 100) - **Priority of the first Machine (master)**
- computer.priority-difference (Default: - 10) - **Space between machines**
- computer.validate-time (Default: 60000) - **Time between machines check**
- command.file-path (Default: C:/) - **Path to the logs of processes**

# Enpoints

 - `/computer/status?token={INSERT_TOKEN}`
Force  check status of machine.

# Process
To validate if there is a machine active, 

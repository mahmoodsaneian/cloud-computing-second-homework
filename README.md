# Cloud Computing Second Homework  

## Overview  
This project is a **Java Spring Boot** application developed as part of a cloud computing assignment. It is **containerized using Docker** and deployed on **Kubernetes** for scalability. The application is designed to handle specific business logic with efficient request handling and persistence.  

## Features  
- **Spring Boot Backend**  
- **Dockerized for Containerization**  
- **Kubernetes Deployment using Helm**  
- **Optimized Database Interactions**  

## Technologies Used  
- **Java 17**, **Spring Boot**  
- **Docker, Kubernetes**  
- **Helm**  

## Deployment  

### Prerequisites  
- Docker & Docker Compose  
- Kubernetes cluster (Minikube, K3s, or cloud provider)  
- Helm installed  

### Running with Docker Compose  
1. Clone the repository:  
   ```sh
   git clone https://github.com/mahmoodsaneian/cloud-computing-second-homework.git
   cd cloud-computing-second-homework
   ```  
2. Start the services using Docker Compose:  
   ```sh
   docker-compose up -d
   ```  

### Deploying on Kubernetes  
1. Build and push the Docker image:  
   ```sh
   docker build -t your-dockerhub-username/app-name:latest .  
   docker push your-dockerhub-username/app-name:latest  
   ```  
2. Deploy using Helm:  
   ```sh
   helm install app-name helm/  
   ```  

## Project Structure  
```
cloud-computing-second-homework/
│── src/
│   ├── main/java/com/app/ (Application logic)
│── helm/ (Helm chart for Kubernetes deployment)
│── docker-compose.yml
│── README.md
│── pom.xml
```  

## Contributing  
Feel free to fork the repository and submit pull requests.  

## License  
This project is licensed under the MIT License.  

## Repository  
[GitHub: cloud-computing-second-homework](https://github.com/mahmoodsaneian/cloud-computing-second-homework)  

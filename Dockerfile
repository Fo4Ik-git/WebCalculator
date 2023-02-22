FROM openjdk:17
WORKDIR /app
COPY . /app

CMD ["java", "-jar", "build/libs/MomCalculator-0.0.1.jar"]
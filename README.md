Here's the revised version of the text with some grammar corrections:

# Flixorama

* Flixorama is a producer-consumer application that simulates the notification of a streaming service based on your country and favorite content genres.
* It utilizes the Fanout and Headers Exchange. The Fanout sends the message to the audit consumer and the Headers Exchange sends the message to the clients.

## Requirements
* Docker
* Java 18 or later

## Running the Application
First, start Docker:
```
docker-compose up
```
Then, run the application.
> Remember to enable multiple instance running in your code editor.

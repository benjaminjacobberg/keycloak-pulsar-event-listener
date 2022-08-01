# keycloak-pulsar-event-listener

## Install 

- Build the project `$ ./mvnw clean package`
- Run the Keycloak and Pulsar `$ docker-compose up -d`
- Open your web browser and navigate to `http://localhost:8080/`
- Click on `Administration Console`
  - Username: `admin`
  - Password: `admin`
- Click on the `Realm Settings` button in the left navigation bar
- Click on the `Events` tab
- Select the dropdown `Event listeners`
- Choose `pulsar`
- Click on the `Save` button
- (Optional) If you have pulsar installed, you can run the following command to view messages in the topic `$ ./pulsar-admin topics peek-messages -n 100 -s my-subscription persistent://public/default/identity-provider-topic`
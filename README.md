# e-Gym
A basic Jetpack Compose CRUD app, developed just for study the technology, where is possible browse exercises, see their details and create training plans (the feature of start a training isn's developed yet) to keep in shape for the summer, or for some else purpose.

- [Technologies](#technologies)
- [Patterns](#patterns)
- [Setup](#setup)

# Technologies
This app implements the following technologies to be able to run as proposed:
* Retrofit
  * Loads the exercise catalog from a private webservice.
* Firebase
  * Read, write and update the training plans as well as perform the user authentication with a google account.
* Jetpack Compose
  * To render the views
* Dagger Hilt
  * Builds the features dependency graph
* Google Accompanist
  * Draws some UI components and controls some Android Window Insets
* Node JS with Exepress
  * Helper api http/https server (just for fetch the exercises yet)
* Docker
  * Just to deploy the http/https server
  
# Patterns
To performs the proposed functions the app was implemented using the ```MVVM```, ```use-cases``` and ```Repository``` patterns.

# Setup
* Retrofit
  * Set the exercises fetching server's url in the ```/core/build.gradle.kt``` file, at the ```HELPER_API_URL``` build config field variable.
  * Create a file called ```api-keys.properties``` into the root app's path with a value ```key1=[server-key]```, where ```server-key``` is your server private key.
* Firebase
  * Place the firebase json config file, with ```auth``` and ```firestore``` permission scopes on it, in the ```/app/src/[debug/release]``` path.
* NodeJs JS with Exepress
  * As the helper api server is a private server, place the keys of it in the ```/functions/oracle-cloud/api/api-keys.json``` file. The required format is an ```array``` of ```string```s. See the ```/functions/oracle-cloud/api/index.js``` file for more details.
  * Place the firebase json config file, with ```firestore``` permission scope on it, in the ```/functions/oracle-cloud/api/e-gym.json``` file.

# Opportunistic Connectivity Service
## OrganiCity - OppNet Application v0.2

## Introduction
###### OrganiCity

[OrganiCity](organicity.eu) is an EU Horizon 2020 project that puts people at the centre of the development of future cities.

###### Opportunistic connectivity service

Opportunistic connectivity service is a tool in OrganiCity to enable co-creation of smart citizens. 
The opportunistic network enabler uses smartphones and IoT devices to build a dynamic wireless communication "infrastructure" at an OC partner site. The software component that enables opportunistic communication will allow the smartphones of OC participants and IoT devices to become part of the networking infrastructure at OC partner sites.

<img src="https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/integration.png" width="250">

## Application Architecture

The application architecture is shown below:

<img src="https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/oppnet_architecture.png" width="250">

The major components of the application include an account manager, a data manager, a device manager and a communication manager. Besides, an incentivisation scheme is also introduced here.

###### Account manager

Account manager handles the federated identity for using the application in accordance with OC platform accounts. To use the application, user has to log in through OC platform and acquire relevant token for further purpose.

###### Data manager

Data manager manages the accounting of the data. It stores data when received or grants data when sent. It also uploads data to Orion context broker when free WiFi is available. 

###### Device manager

Device manager handles the registration of the device by communicating with Orion context broker.

###### Communication manager

It schedules the bluetooth scanning and handles existing communication tasks. It uses callbacks to send data or response to the data manager to help it maintain data record. 

###### Experiment manager

Experiment manager manages the interaction between oppnet and OC experiment platform. It will be developed when the experiment platform comes out.

###### Incentivisation
The incentivisation algorithm is designed based on the selfish mule protocol introduced in this [paper](https://www.google.co.uk/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwix3qCktMbNAhXFDsAKHWeXCUYQFggfMAA&url=http%3A%2F%2Fieeexplore.ieee.org%2Fxpls%2Fabs_all.jsp%3Farnumber%3D6517116&usg=AFQjCNGuwb4q2gVJcBC2ildR4pA_usTD1A&sig2=SUX10aK4Z4zcO1hJ1_3rzA), which is based on a Backpressure routing protocol. Incentivisation is calculated locally and then relevant data will be transmitted to context broker during the experiment. 

## User Guide

#### Overview

There are two possible ways to use OppNet to perform experiments on OrganiCity platform:

- OppNet legacy: default OppNet application developed for sensor data relaying
- Customized OppNet application: experimenters develop their own application using the framework OppNet provides

#### OppNet legacy

The OppNet legacy application is intended to be used only for sensor data relaying purpose. In order to use the application, one must provide correctly formatted data from their sensors in order for OppNet to talk to their sensors.

Following things must be done at the sensor side in order to achieve this:

##### Bluetooth adapter name
The Bluetooth adapter name must be the following format: 
```javascript
OppNet:[device type]:[device ID]:[queue size]:[battery level]
```
For a sensor, it should be OppNet:S. Then the OppNet application will know that it is a sensor and thus tries to connect to it.
##### Bluetooth packet format
The packet sent to the OppNet application must be in the following format: 
```javascript
{
	"type": 102,
	"data": [
		{
			"path": unique_device_id, 
			"data": data_content, 
			"delay": 0, 
			"id": data_packet_id
		},
		...
	]
}
```
Then the application is able to correctly parse the packet the sensor sends to it.

#### Customized application

The application can be developed using any Android IDE, such as Eclipse and Android Studio. There are several dependencies one need to add when compiling the provided code:

- Google play services: one need to add google play services to the application in order to use the Google Map API for Android. More information can be found at [Google Map API](https://developers.google.com/maps/documentation/android-api/start)
- jackson-annotations-X.X.X.jar: this is needed for account API
- jackson-core-X.X.X.jar: for accounts
- jackson-databind-X.X.X.jar: for accounts
- jjwt-X.X.X.jar: to parse information from assigned token
- json.jar: create or parse json message



###### Install the app

The app can be installed either through downloading the [.apk](https://github.com/OrganicityEu-Platform/oppnet/releases/download/v1.0/OppNet.apk) or compiling on your local machine. 

There are five main UI:

1. Experiment list UI: it shows the list of all available experiments.
2. Account UI: it enables authorisation of the app.
3. Network UI: it shows the status of the network.
4. Settings UI: it enables basic settings for the app.
5. Experiment UI: it shows details for a certain experiment.

Screenshots of the application when running are shown here:

Experiment | Experiment List | Account | Network | Settings
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/Screenshot_2017-02-20-12-17-51.png)  |![](https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/Screenshot_2017-02-20-12-16-56.png)  |  ![](https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/Screenshot_2017-02-20-12-17-10.png) | ![](https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/Screenshot_2017-02-20-12-17-15.png)  |![](https://raw.githubusercontent.com/OrganicityEu-Platform/oppnet/master/Screenshot_2017-02-20-12-17-19.png)  

see also
 * https://organicityeu.github.io/oppnet/
 * http://organicity.eu/tools/




Android Push Server Demo Example using javocsoft-androideasypushserver library. 



This is an example server that uses javocsoft-androideasypushserver library to register itself as a valid C2DM 
(Cloud to Device Messaging) server to send PUSH notifications to Android Devices.

This server is a simple CRUD 
RESTFull demo server exposing the following operations for an Android Device:



Registration. 			Path: /andpushreg/register


Un-registration. 		Path: /andpushreg/unregister


Activation. 			Path: /andpushreg/activate


Desactivation. 			Path: /andpushreg/desactivate
Device 

Registration Information. 	Path: /andpushreg/showdeviceinfo


Efficacy receival (ACK) for sent Push Notifications. Path: /andpush/ack



All of these operations back-end are done by implementing some interfaces from the library.


ACKInterface 
RegistrationInterface 




When server initializes it will ask to C2DM for a valid authentication token. Once the server has it, it will 
store (or not, is configurable by the library) this token and use when needed. 

To send pushes to an android device there are some test classes to use.

GetServerAuthenticationToken. This will reproduce, with more details, the process done in the library to get an 
authentication token from C2DM servers.
SendPush, this will reproduce, with more details, the process done in the library to send a Push.
TestC2DMClient, This will test the C2DM client.

URL of testServer: http://localhost:8080/AndroidPushServer/

IMPORTANT:
----------
When running the server from localhost do not use Eclipse, use an external server because if not, the emulator
is not able to connect with it.

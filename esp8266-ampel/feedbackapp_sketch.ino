#include <ESP8266WiFi.h>
#include <gpio.h>
#include <aREST.h>

//const char *ssid =  "WIV";     // replace with your wifi ssid and wpa2 key
//const char *pass =  "DF~12qa.40";

const char *ssid =  "feedbackapp";     // replace with your wifi ssid and wpa2 key
const char *pass =  "12345678";

WiFiClient client; 
aREST rest=aREST();
#define LISTEN_PORT 80
WiFiServer server(LISTEN_PORT);
int ledControl(String command);
 
void setup() 
{
       Serial.begin(9600);
       delay(10);
               
       Serial.println("Connecting to ");
       Serial.println(ssid); 
 
       WiFi.begin(ssid, pass); 
       while (WiFi.status() != WL_CONNECTED) 
          {
            delay(500);
            Serial.print(".");
          }
      Serial.println("");
      Serial.println("WiFi connected"); 
      Serial.println(WiFi.status());
      Serial.println(WiFi.localIP());

      pinMode(5, OUTPUT); //rot
      pinMode(4, OUTPUT);//gelb
      pinMode(0, OUTPUT);//grün

      rest.function("led", ledControl);
      rest.set_id("1");
      rest.set_name("esp8266");
      server.begin();
      
}
 
void loop() {
 WiFiClient client = server.available();
 if (!client) {
  return;
 }
 while (!client.available()) {
  delay(1);
 }
 rest.handle(client);

 }

int ledControl(String command){
  Serial.println(command);
  if (command=="0") {
    digitalWrite(0, LOW);
    digitalWrite(5, LOW);
    digitalWrite(4, LOW);
  }
  else if (command=="1") {
    digitalWrite(0, LOW);
    digitalWrite(4, LOW);
    digitalWrite(5, HIGH);
    
  }
  else if (command=="2") {
    digitalWrite(0, LOW);
    digitalWrite(5, LOW);
    digitalWrite(4, HIGH);
  }
  else if (command=="3") {
    digitalWrite(5, LOW);
    digitalWrite(4, LOW);
    digitalWrite(0, HIGH);
  }
  
}

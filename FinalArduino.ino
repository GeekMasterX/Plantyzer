#include <dht.h>
#include <SoftwareSerial.h>
#define DHT11_PIN 8
int sensor_pin = A1;
dht DHT;
boolean startCapture;
unsigned long lastTimeMillis = 0;
unsigned long lastTimeMillis2 = 0;
unsigned long lastTimeMillis3 = 0;
long cm;
int ctrl;
int moistureGet;
String temp, duplicate;
int output_value, j, k;
float temperature, humidity;
int motorPin = 12;
const int trigPin = 5;
const int echoPin = 6;

SoftwareSerial ESP8266 (2, 3);

void printResponse() {
  while (ESP8266.available()) {
    Serial.println(ESP8266.readStringUntil('\n'));
  }
}

long microsecondsToCentimeters(long microseconds)
{

  return microseconds / 29 / 2;
}

void setup() {
   pinMode(motorPin,OUTPUT);
   Serial.begin(9600);
   delay(2000);

   }

void loop() {

   //Ultrasonic
   if (millis() - lastTimeMillis > 500){
   lastTimeMillis = millis();
   pinMode(trigPin, OUTPUT);
   digitalWrite(trigPin, LOW);
   delayMicroseconds(4);
   digitalWrite(trigPin, HIGH);
   delayMicroseconds(20);
   digitalWrite(trigPin, LOW);
   pinMode(echoPin, INPUT);
   long duration = pulseIn(echoPin, HIGH);
   cm = microsecondsToCentimeters(duration);
   Serial.print(cm);
   Serial.print("cm");
   Serial.println();
  }
  
   if (millis() - lastTimeMillis2 > 2000){
    lastTimeMillis2 = millis();

    
  //Moisture Sensor
   output_value= analogRead(sensor_pin);
   //output_value = map(output_value,550,450,0,100);
   Serial.print("Mositure : ");
   Serial.print(output_value);
   Serial.println("%");

   //DHT11
   int chk = DHT.read11(DHT11_PIN);
   Serial.print("Temperature = ");
   temperature = DHT.temperature;
   Serial.println(DHT.temperature);
   Serial.print("Humidity = ");
   humidity = DHT.humidity;
   Serial.println(DHT.humidity);
   }

   if (millis() - lastTimeMillis3 > 9000){
    lastTimeMillis3 = millis();

    ESP8266.println("AT+CIPMUX=1");
    delay(1000);
    printResponse();

    ESP8266.println("AT+CIPSTART=0,\"TCP\",\"api.thingspeak.com\",80");
    delay(1000);
    printResponse();

    String cmd = "GET https://api.thingspeak.com/apps/thinghttp/send_request?api_key=6U9BW7LKK8BF8D7F";
    ESP8266.println("AT+CIPSEND=0," + String(cmd.length() + 4));
    delay(1000);
    ESP8266.println(cmd);
    delay(1000);
    ESP8266.println("");

    
   }

   if (ESP8266.available()) {
    char c = ESP8266.read();
    if (c == '{') {
      startCapture = true;
      j++;
    }

    if (startCapture) {
      temp += c;
    }

    if (c == '}') {
      startCapture = false;
      duplicate = temp;
      temp = "";
      k++;
    }
  }


  else {

    Serial.print("Received bytes");
    Serial.print(duplicate.length());
    Serial.println("Disconnecting.");
    ESP8266.flush();
    Serial.println(temp);
    Serial.println(duplicate);

    String b = duplicate.substring(3, 4);
    if(b.equals("1")){
      ctrl=0;
      digitalWrite(motorPin,HIGH);
    }
    else if(b.equals("0")){
      ctrl=1;
      digitalWrite(motorPin,LOW);
    }
    String c = duplicate.substring(8, 10);

    char floatbuf[32]; // make this at least big enough for the whole string
    c.toCharArray(floatbuf, sizeof(floatbuf));
    int f = atoi(floatbuf);
    Serial.println(f);
//    char e[2];
//    for(int i = 0;i<b.length();i++){
//      e[i]=c.charAt(i);
//    }
//    int cs = (int) stoi(e,NULL,10);
    int cs = c.toInt();
    Serial.println(cs);


  }
   if(ctrl==1){
   if(cm>10){
    digitalWrite(motorPin,LOW);
   }
   else{
    //if(temperature>=30 || moistureGet<40){
      digitalWrite(motorPin,HIGH);      
    }
   }
}

#include <dht.h>
#define DHT11_PIN 8
int sensor_pin = A0;
dht DHT;
long cm;
int ctrl;
int moistureGet;
String temp, duplicate;
int output_value, j, k;
float temperature, humidity;
int motorPin = 12;
const int trigPin = 5;
const int echoPin = 6;

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
    
  //Moisture Sensor
   output_value= analogRead(sensor_pin);
   output_value = map(output_value,1023,380,0,100);
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

   if(output_value < 50 )
   {
      digitalWrite(motorPin, LOW);
   }
   else {
    digitalWrite(motorPin, HIGH);
   }
    
   
   delay(2000);
}

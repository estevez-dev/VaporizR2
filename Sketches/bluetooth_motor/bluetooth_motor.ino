#include <SparkFun_TB6612.h>

#include <SoftwareSerial.h>

#define AIN1 4
#define BIN1 8
#define AIN2 5
#define BIN2 9
#define PWMA 7
#define PWMB 10
#define STBY 6

const int offsetA = 1;
const int offsetB = 1;

SoftwareSerial BTSerial(3, 2); // RX | TX
String reader;
int d = 0;
Motor motor1 = Motor(AIN1, AIN2, PWMA, offsetA, STBY);
Motor motor2 = Motor(BIN1, BIN2, PWMB, offsetB, STBY);

void setup()
{
  Serial.begin(9600);
  Serial.println("Ready:");
  BTSerial.begin(9600);  // HC-05 default speed in AT command more
}

void loop()
{
  reader="";
  while (BTSerial.available()) {
    delay(3);
    if (BTSerial.available()>0) {
      char c = BTSerial.read();
      reader += c;
        
    }
  }
  if (reader.length() > 0) {
    Serial.println("Receved: "+reader+"");
    d = reader.toInt();    
  }
 
  if (d == 11) {
    motor1.drive(255);
  } else if (d == 12) {
    motor1.drive(-255);
  }
  
}

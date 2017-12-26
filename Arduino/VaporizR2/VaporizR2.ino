#include <SparkFun_TB6612.h>

#include <SoftwareSerial.h>

#define AIN1 4
#define BIN1 8
#define AIN2 5
#define BIN2 7
#define PWMA 11
#define PWMB 10
#define STBY 6

const int offsetA = 1;
const int offsetB = -1;

SoftwareSerial BTSerial(3, 2); // RX | TX
String reader;
Motor motor1 = Motor(AIN1, AIN2, PWMA, offsetA, STBY);
Motor motor2 = Motor(BIN1, BIN2, PWMB, offsetB, STBY);

void setup()
{
  //Serial.begin(9600);
  //Serial.println("Ready:");
  BTSerial.begin(9600);  // HC-05 default speed in AT command more
  brake(motor1, motor2);
}

void loop()
{
  char m1 = '9';
  char m2 = '9';
  //Serial.println("There is "+String(BTSerial.available())+" available");
  if (BTSerial.available()==2) {
      m1 = BTSerial.read();
      m2 = BTSerial.read();
      //Serial.println("Receved: "+String(m1)+";"+String(m2));
      switch (m1) {
        case '0':
          motor1.drive(-255);
          break;
        case '1':
          motor1.drive(-170);
          break;
        case '2':
          motor1.drive(0);
          break;
        case '3':
          motor1.drive(170);
          break;
        case '4':
          motor1.drive(255);
          break;
      }
      switch (m2) {
        case '0':
          motor2.drive(-255);
          break;
        case '1':
          motor2.drive(-170);
          break;
        case '2':
          motor2.drive(0);
          break;
        case '3':
          motor2.drive(170);
          break;
        case '4':
          motor2.drive(255);
          break;
      }    
  }

}

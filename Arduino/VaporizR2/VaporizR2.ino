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
Motor motorA = Motor(AIN1, AIN2, PWMA, offsetA, STBY);
Motor motorB = Motor(BIN1, BIN2, PWMB, offsetB, STBY);

void setup()
{
  //Serial.begin(9600);
  //Serial.println("Ready:");
  BTSerial.begin(9600);  // HC-05 default speed in AT command more
}

void loop()
{
  char motorADirection = 'f';
  char motorBDirection = 'f';
  char motorASpeed = '0';
  char motorBSpeed = '0';
  int motorAVelocity = 0;
  int motorBVelocity = 0;
  //Serial.println("There is "+String(BTSerial.available())+" available");
  if (BTSerial.available() == 4) {
      motorADirection = BTSerial.read();
      motorASpeed = BTSerial.read();
      motorBDirection = BTSerial.read();
      motorBSpeed = BTSerial.read();
      //Serial.println("Receved: "+String(motorADirection)+String(motorASpeed)+String(motorBDirection)+String(motorBSpeed));
      switch (motorASpeed) {
        case '0':
          motorAVelocity = 0;
          break;
        case '1':
          motorAVelocity = 100;
          break;
        case '2':
          motorAVelocity = 140;
          break;
        case '3':
          motorAVelocity = 180;
          break;
        case '4':
          motorAVelocity = 200;
          break;
        case '5':
          motorAVelocity = 225;
          break;
      }
      switch (motorBSpeed) {
        case '0':
          motorBVelocity = 0;
          break;
        case '1':
          motorBVelocity = 100;
          break;
        case '2':
          motorBVelocity = 140;
          break;
        case '3':
          motorBVelocity = 180;
          break;
        case '4':
          motorBVelocity = 200;
          break;
        case '5':
          motorBVelocity = 225;
          break;
      }
      if (motorADirection == 'b') {
        motorAVelocity = 0 - motorAVelocity;
      }
      if (motorBDirection == 'b') {
        motorBVelocity = 0 - motorBVelocity;
      }
      //Serial.println("A: "+String(motorAVelocity)+"; B: "+String(motorBVelocity));
      motorA.drive(motorAVelocity);
      motorB.drive(motorBVelocity);
  }

}

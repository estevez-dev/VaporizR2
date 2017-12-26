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

Motor motor1 = Motor(AIN1, AIN2, PWMA, offsetA, STBY);
Motor motor2 = Motor(BIN1, BIN2, PWMB, offsetB, STBY);

void setup()
{
  Serial.begin(9600);
  Serial.println("Ready:");
}

void loop()
{
  int v = 0;
  while(v<=255) {
    motor2.drive(v);
    Serial.println("Speed: "+String(v));
    delay(100);
    v = v+1;
  }
}

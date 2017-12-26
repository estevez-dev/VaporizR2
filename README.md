# VaporizR2
## Parts
1. Arduino Uno R3
1. Sparkfun Motor Driver ([library and examples](https://learn.sparkfun.com/tutorials/tb6612fng-hookup-guide))
1. HC-05 Serial Bluetooth Module
1. PP3 9-volt Battery
## Source code
In source code you can find:
* an Android app to control the car
* a [Fritzing sketch](/estevez-dev/VaporizR2/tree/v1.0/Fritzing) to build hardware
* an [Arduino sketch](/estevez-dev/VaporizR2/tree/v1.0/Sketches/bluetooth_motor)
## Car hardware
![image](https://github.com/estevez-dev/VaporizR2/blob/v1.0/docs/VaporizR2_bb.png?raw=true)
## Android app
It is a simple app with two sliders to control left and right weels that sends commands to Arduino via Bluetooth serial.

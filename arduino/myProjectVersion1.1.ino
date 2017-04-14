#include <AFMotor.h>
#include <Wire.h>  
//机甲无人驾驶 蓝牙控制  电路图： myNewProject.fzz

   //适用于通道1和2的频率： MOTOR12_64KHZ  MOTOR12_8KHZ  MOTOR12_2KHZ  MOTOR12_1KHZ
   //适用于通道3和4的频率：MOTOR34_64KHZ   MOTOR34_8KHZ  MOTOR34_1KHZ   
   //更高的频率会减小电机在运动过程中的噪音，但同时也会降低扭矩 
   AF_DCMotor motorleft(3);
   AF_DCMotor motorright(4);

void setup() {
  // put your setup code here, to run once:
   Serial.begin(9600);
   //速度暂时为硬编码，将来拓展；目前只支持匀速运动
   motorleft.setSpeed(200);
   motorright.setSpeed(200);

  // turn on servo  
  //设定舵机的接口，只有9或10接口可利用。
  servo1.attach(9);
   
}

void loop() {
  // put your main code here, to run repeatedly: 
    while(Serial.available()>0){
      char c = Serial.read();
      Serial.write(c);
      //操作电机 
      if(c=="D"){
          motorleft.run(FORWARD);
          motorright.run(FORWARD);
      }else if("R"){//reverse 
         motorleft.run(BACKWARD);
         motorright.run(BACKWARD);
      }else if("P"){
         motorleft.run(RELEASE);
         motorright.run(RELEASE);
      }
   }

  //pos 为舵机旋转的角度  0-180 180-0
  for(pos = 0; pos < 180; pos += 1)  // goes from 0 degrees to 180 degrees 
  {                                  // in steps of 1 degree 
    myservo.write(pos);              // tell servo to go to position in variable 'pos' 
    delay(15);                       // waits 15ms for the servo to reach the position 
  } 
  for(pos = 180; pos>=1; pos-=1)     // goes from 180 degrees to 0 degrees 
  {                                
    myservo.write(pos);              // tell servo to go to position in variable 'pos' 
    delay(15);                       // waits 15ms for the servo to reach the position 
  } 

}

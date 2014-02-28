byte peak = 0;

void setup() {
  Serial.begin(9600); 
  delay(50);
}

void loop() {
  int sensorValue = analogRead(A0);
  float voltage = sensorValue * (5.0 / 1023.0);
  if(voltage > 2.0){
    if (!peak) {
      peak = 1;
      Serial.print("0:");
      Serial.println(voltage);
    }
  } else {
    peak = 0;
  }
}    


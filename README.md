exdrummer
=========

# Скетч отправки данных в BT

```
int counter =0;

void setup() {
  Serial.begin(9600); 
  delay(50);
}

void loop() {
  counter++;
  Serial.print("Arduino counter: ");
  Serial.println(counter);
  delay(500); // wait half a sec
}
```


Отладка передачи данных с помощью компьютера

1. Подключаем
Подключение:
RXD -> Digital TX
TXD -> Digital RX
GND -> GND
VCC -> 3.3v

2. Прошиваем Arduino этим скетчем
3. Открываем BT manager на убунте, находим устройство HC-06, соединяемся с ним, спариваемся (пин 1234), пробрасываем его в serialport. Оно пробросится в /dev/rfcomm0
4. Открываем cutecom из под рута (обязательно иначе не подключится к порту)
5. Начинаем слушать порт /dev/rfcomm0
6. 


# Подключение пьезодатчика и работа с ним

http://arduino.cc/en/uploads/Tutorial/knock_bb.png


```
Обработка сигнала с датчика

void setup(){
  Serial.begin(9600);
}

void loop(){
  int sensorValue = analogRead(A0);
  float voltage = sensorValue * (5.0 / 1023.0);
  Serial.println(voltage);  
}```





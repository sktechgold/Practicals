import time
from gpiozero import LED
from w1thermsensor import W1ThermSensor

# Initialize temperature sensor
sensor = W1ThermSensor()

# Define LEDs
led_pins = [20, 21, 22, 23, 24, 25, 26, 27]
leds = [LED(pin) for pin in led_pins]

# Turn off all LEDs initially
for led in leds:
    led.off()

try:
    while True:
        temp = sensor.get_temperature()  # Reads temperature in Celsius
        print(f"Current Temperature: {temp:.2f} °C")

        if temp >= 29:
            # Temperature high → turn all LEDs OFF
            for led in leds:
                led.off()
            print("Warning: Temperature limit exceeded!")
        else:
            # Normal temperature → turn all LEDs ON
            for led in leds:
                led.on()

        time.sleep(1)

except KeyboardInterrupt:
    print("\nProgram stopped by user.")
    for led in leds:
        led.off()

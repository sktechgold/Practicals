import time
import RPi.GPIO as GPIO

# Define LED pins
led_pins = [20, 21, 22, 23, 24, 25, 26, 27]

# Setup
GPIO.setmode(GPIO.BCM)
for pin in led_pins:
    GPIO.setup(pin, GPIO.OUT)
    GPIO.output(pin, GPIO.LOW)

def ledState(led_index, state):
    GPIO.output(led_pins[led_index], state)

try:
    print("All LEDs ON for 1 second at start...")
    for i in range(8):
        ledState(i, 1)
    time.sleep(1)

    print("Starting LED blink sequence. Press Ctrl+C to stop.")
    while True:
        for i in range(8):
            ledState(i, 1)
            time.sleep(0.5)
            ledState(i, 0)
            time.sleep(0.5)

except KeyboardInterrupt:
    print("\nProgram stopped by user.")

finally:
    GPIO.cleanup()
    print("GPIO cleanup done. Exiting safely.")

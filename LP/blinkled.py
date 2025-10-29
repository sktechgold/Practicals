import time
import RPi.GPIO as GPIO

# Constants
TRUE = 1

# LED pin numbers
led1 = 20
led2 = 21
led3 = 22
led4 = 23
led5 = 24
led6 = 25
led7 = 26
led8 = 27

# List of all LED pins
myled = [led1, led2, led3, led4, led5, led6, led7, led8]

# GPIO setup
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

for pin in myled:
    GPIO.setup(pin, GPIO.OUT)

# Function to set LED state
def ledState(led, state):
    GPIO.output(myled[led], state)

try:
    # Turn all LEDs ON once at the start
    for i in range(8):
        ledState(i, 1)
    time.sleep(1)
    # Turn them OFF before starting blink sequence
    for i in range(8):
        ledState(i, 0)

    # Main blinking loop
    while TRUE:
        for i in range(8):
            ledState(i, 1)
            time.sleep(0.5)
            ledState(i, 0)
            time.sleep(0.5)

except KeyboardInterrupt:
    print("\nProgram closed by user.")

finally:
    GPIO.cleanup()
    print("GPIO cleaned up. Exiting...")

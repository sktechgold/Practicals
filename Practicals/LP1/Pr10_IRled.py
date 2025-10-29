import time
import RPi.GPIO as GPIO

# Pin configuration
DETECT_PIN = 5   # IR sensor output pin
LED_PIN = 8      # LED indicator pin

# Setup function
def init_system():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(DETECT_PIN, GPIO.IN, pull_up_down=GPIO.PUD_UP)
    GPIO.setup(LED_PIN, GPIO.OUT)
    GPIO.output(LED_PIN, GPIO.LOW)
    print("System Initialized...")

# Function to detect object
def detect_object():
    if GPIO.input(DETECT_PIN) == 0:
        return True   # Object detected
    else:
        return False  # No object

try:
    print("\nObject Detection using IR Sensor")
    print("---------------------------------\n")

    init_system()
    count = 0

    while True:
        if detect_object():
            count += 1
            print(f"Object detected! Count = {count}")
            
            GPIO.output(LED_PIN, GPIO.HIGH)  # Turn LED ON
            time.sleep(0.5)
            GPIO.output(LED_PIN, GPIO.LOW)   # Turn LED OFF
            time.sleep(1)                    # Debounce delay

except KeyboardInterrupt:
    print("\nProgram stopped by user.")

finally:
    GPIO.cleanup()
    print("GPIO cleanup done. Exiting safely.")

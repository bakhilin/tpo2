
filename = '/home/bakhilin/tpo2/tpo2/csv/func.csv'

import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv(filename, sep=',', names=['x', 'f(x)'], skiprows=1)
x = df['x']
y = df['f(x)']

plt.figure(figsize=(12, 6))
plt.scatter(x, y, s=20, color='red', alpha=0.7)

# Настройка осей
plt.yscale('symlog', linthresh=1e-8)  # Логарифмическая шкала для Y
plt.xlabel('x', fontsize=12)
plt.ylabel('f(x) (symlog)', fontsize=12)
plt.title('Точечный график с логарифмической осью Y', fontsize=14)
plt.grid(True, linestyle='--', alpha=0.5)

plt.tight_layout()
plt.show()
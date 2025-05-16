import matplotlib.pyplot as plt
import numpy as np

def read_data_from_file(filename):
    """Чтение данных x и f(x) из файла"""
    x = []
    fx = []
    with open(filename, 'r') as file:
        for line in file:
            if line.strip():  # Пропускаем пустые строки
                parts = line.strip().split(',')
                x.append(float(parts[0]))
                fx.append(float(parts[1]))
    return np.array(x), np.array(fx)

# Чтение данных из файла
filename = 'data.txt'  # Укажите имя вашего файла
x, fx = read_data_from_file(filename)

# Построение графика
plt.figure(figsize=(12, 6))
plt.plot(x, fx, 'bo-', label='f(x)', markersize=4)

# Настройка графика
plt.title('График функции f(x)')
plt.xlabel('x')
plt.ylabel('f(x)')
plt.grid(True)

# Настройка меток оси X
x_min, x_max = min(x), max(x)
plt.xticks(np.arange(np.floor(x_min*10)/10, np.ceil(x_max*10)/10 + 0.1, 0.2))

plt.legend()
plt.show()
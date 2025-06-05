import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

def read_points_from_file(filename):
    """Чтение точек из файла CSV"""
    try:
        data = pd.read_csv(filename, header=None, names=['x', 'f(x)'], skiprows=1)
        return data['x'].values, data['f(x)'].values
    except Exception as e:
        print(f"Ошибка при чтении файла: {e}")
        return None, None

def plot_graph(x, y, smooth=True):
    """Построение графика с автоматическим масштабированием"""
    plt.figure(figsize=(10, 6))
    
    # Определяем границы с небольшим запасом
    x_min, x_max = min(x), max(x)
    y_min, y_max = min(y), max(y)
    
    # Добавляем 10% запаса по осям для лучшей видимости
    x_margin = 0.1 * (x_max - x_min) if (x_max - x_min) != 0 else 0.5
    y_margin = 0.1 * (y_max - y_min) if (y_max - y_min) != 0 else 0.5
    
    plt.xlim(x_min - x_margin, x_max + x_margin)
    plt.ylim(y_min - y_margin, y_max + y_margin)
    
    if smooth and len(x) > 2:
        # Интерполяция для гладкого графика
        x_smooth = np.linspace(x_min, x_max, 500)
        y_smooth = np.interp(x_smooth, x, y)
        plt.plot(x_smooth, y_smooth, 'b-', linewidth=2, label='Интерполяция')
    
    # Отображение исходных точек
    plt.plot(x, y, 'ro', markersize=5, label='Точки из файла')
    
    # Выделение экстремумов и точек перегиба
    if len(x) > 1:
        diff = np.diff(y)
        increasing = diff > 0
        decreasing = diff < 0
        
        # Размечаем участки возрастания/убывания
        for i in range(len(increasing)-1):
            if increasing[i] and not increasing[i+1]:
                plt.axvline(x=x[i+1], color='g', linestyle='--', alpha=0.3, label='Максимум' if i==0 else "")
            elif decreasing[i] and not decreasing[i+1]:
                plt.axvline(x=x[i+1], color='r', linestyle='--', alpha=0.3, label='Минимум' if i==0 else "")
    
    # Настройка внешнего вида
    plt.title('График функции с оптимальным масштабом', fontsize=14)
    plt.xlabel('x', fontsize=12)
    plt.ylabel('f(x)', fontsize=12)
    plt.grid(True, linestyle='--', alpha=0.7)
    
    # Убираем дублирование подписей в легенде
    handles, labels = plt.gca().get_legend_handles_labels()
    by_label = dict(zip(labels, handles))
    plt.legend(by_label.values(), by_label.keys())
    
    # Показать график
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    import sys
    
    if len(sys.argv) < 1:
        print("Использование: python script.py <filename.csv>")
        print("Пример: python script.py data.csv")
        sys.exit(1)
    
    filename = 'data.txt'
    x, y = read_points_from_file(filename)
    
    if x is not None and y is not None:
        plot_graph(x, y)
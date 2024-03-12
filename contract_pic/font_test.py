import matplotlib.pyplot as plt
import matplotlib.font_manager as fm

# 设置SimSun字体
simsum_font_path = 'simsun.ttc'  # 请替换为你的SimSun字体文件路径
simsum_prop = fm.FontProperties(fname=simsum_font_path, size=12)

# 设置Times New Roman字体
times_font_path = 'times.ttf'  # 请替换为你的Times New Roman字体文件路径
times_prop = fm.FontProperties(fname=times_font_path, size=12)

# 创建一个图形
plt.figure()

# 在标题中同时使用不同字体显示文字
plt.title('SimSun字体标题', fontproperties=simsum_prop)
plt.title('Times New Roman', fontproperties=times_prop, loc='right')

# 显示图形
plt.show()
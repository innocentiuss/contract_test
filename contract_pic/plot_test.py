import matplotlib.pyplot as plt

# 数据
categories = ['A', 'B', 'C', 'D']
values1 = [23, 45, 56, 78]
values2 = [15, 30, 45, 60]

# 设置每个柱子的宽度
bar_width = 0.35

# 设置柱子的位置
bar_positions1 = range(len(categories))
bar_positions2 = [pos + bar_width for pos in bar_positions1]

# 绘制柱状图
plt.bar(bar_positions1, values1, width=bar_width, label='Group 1')
plt.bar(bar_positions2, values2, width=bar_width, label='Group 2')

# 添加标题和标签
plt.title('Sample Bar Chart with Multiple Groups')
plt.xlabel('Categories')
plt.ylabel('Values')
plt.xticks([pos + bar_width / 2 for pos in bar_positions1], categories)

# 添加图例
plt.legend()

# 显示图形
plt.show()
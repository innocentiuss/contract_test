import matplotlib.pyplot as plt
import matplotlib.font_manager as fm

# 设置SimSun字体
simsum_font_path = 'simsun.ttc'  # 请替换为你的SimSun字体文件路径
simsum_prop = fm.FontProperties(fname=simsum_font_path, size=12)
# 设置Times New Roman字体
times_font_path = 'times.ttf'  # 请替换为你的Times New Roman字体文件路径
times_prop = fm.FontProperties(fname=times_font_path, size=12)
plt.rcParams['font.family'] = 'simsun'

# 数据
categories = ['10', '50', '100']
values1 = [360, 1800, 3600]
values2 = [4620, 23100, 46200]

# 设置每个柱子的宽度
bar_width = 0.35

# 设置柱子的位置
bar_positions1 = range(len(categories))
bar_positions2 = [pos + bar_width for pos in bar_positions1]

# 绘制柱状图
bars1 = plt.bar(bar_positions1, values1, width=bar_width, label='证书哈希值')
bars2 = plt.bar(bar_positions2, values2, width=bar_width, label='证书原件')

# 添加折线图
# plt.plot(bar_positions1, values1, marker='o', color='b', label='证书哈希值折线')
# plt.plot(bar_positions2, values2, marker='o', color='r', label='证书原件折线')

# 添加标题和标签
# plt.title('证书原件与证书哈希存储空间使用对比', fontproperties=simsum_prop)
plt.xlabel('证书个数', fontproperties=simsum_prop)
plt.ylabel('占用空间(字节)', fontproperties=simsum_prop)
plt.xticks([pos + bar_width / 2 for pos in bar_positions1], categories)

# 为每个柱子上的文字设置字体属性
# for bar in bars1:
#     plt.annotate(bar.get_height(),
#                  (bar.get_x() + bar.get_width() / 2, bar.get_height()),
#                  ha='center', va='bottom',
#                  fontsize=10, fontproperties=simsum_prop)
# for bar in bars2:
#     plt.annotate(bar.get_height(),
#                  (bar.get_x() + bar.get_width() / 2, bar.get_height()),
#                  ha='center', va='bottom',
#                  fontsize=10, fontproperties=simsum_prop)

# 添加图例
plt.legend()

# 显示图形
plt.show()

import re
import matplotlib.pyplot as plt
import matplotlib.font_manager as fm
# 设置全局字体参数
plt.rcParams['font.family'] = 'Times New Roman'

# 加载宋体字体
simsun_font = fm.FontProperties(fname='SimSun.ttc')


# 读取log文件
with open("asn_proto_json_full.log", "r", encoding='utf-8') as f:
    log = f.read()

# 使用正则表达式提取需要的数据
pattern = re.compile(r'(\d+) 个 (.*?) 证书(.*?)耗时 (\d+) ms')
matches = pattern.findall(log)

# 初始化数据存储字典
# data = {"add": {"json": [], "protobuf": [], "flat buffers": []},
#         "verify": {"json": [], "protobuf": [], "flat buffers": []},
#         "revoke": {"json": [], "protobuf": [], "flat buffers": []}}
data = {"add": {"json": [], "protobuf": [], "der": []},
        "verify": {"json": [], "protobuf": [], "der": []},
        "revoke": {"json": [], "protobuf": [], "der": []}}

cnt = 0
# 填充数据
for match in matches:
    # cnt += 1
    # if cnt < 203:
    #     continue
    # data[match[2]][match[1]].append(float(match[3]) * 100)
    data[match[2]][match[1]].append(float(match[3]) / float(match[0]) * 100)

# 绘制趋势图
fig, axs = plt.subplots(1, 3, figsize=(15, 5))
x = [i for i in range(10, 10001, 10)]
for i, operation in enumerate(data.keys()):
    for cert_type in data[operation].keys():
        axs[i].plot(x, data[operation][cert_type], label=cert_type)
    axs[i].set_title(f"certificate {operation} ")
    axs[i].set_xlabel("证书个数", fontproperties=simsun_font)
    axs[i].set_ylabel("ms")
    axs[i].legend()

plt.tight_layout()
plt.show()

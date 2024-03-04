pragma solidity ^0.8.0;
contract Cert_CA {
    // 静态数据，控制序列号的加1
    uint256 private sequenceNumber;
    struct Certificate {
//        证书版本号
        string version;
        // 证书序列号
        uint serialNumber;
        // 证书使用的证书签名hash算法 SHA1
        string signature_alo;
        // 证书颁布者  这里设置为该智能合约的地址
        address issuer;
        // 证书有效的结束日期  只记录证书的有效期
        uint validNotAfter;
        // 证书持有人的公钥 作为证书申请者的唯一标识符
        bytes32 holder;
//        证书主体 签名算法 设置为SHA256
        string holder_alo;
        //       证书 签名值
        // bytes32 signatureValue;
        // 扩展部分得字段
        // 证书所在区块高度 可以通过block.number获取
        uint256 blockHeight;
        // 证书历史操作高度
        uint256 blockPreHeight;
    }
    mapping(bytes32 => Certificate) public certs;     // certId => cert  (certId is keccak256(pubKey)
    // 证书生成事件
    event CertAdded(bytes32);
    //
    event SetAdded(Certificate);
    // 证书撤销事件  把证书抛出去
    event CertRevoked(Certificate);
    // 获取区块高度  这个要把合约放到区块链上才可以
    // function getCurrentBlockHeight() public view returns (uint) {
    //     return block.number;
    // }
    // 智能合约先部署在区块链上，获取区块高度，然后将获取的区块高度 赋值到证书的结构
    // 但是智能合约部署在区块链的话不就运行了吗
    // 直接使用solidity内置的API来获取区块高度 block.number
    // 设置一个证书生成的函数
    function certCreate(bytes32 pubKey) public returns (Certificate memory){
 Certificate memory certificate;
        sequenceNumber++;
        bytes memory pubKeyBytes = abi.encodePacked(pubKey);
        bytes32 temp = keccak256(pubKeyBytes);
        // Certificate memory certificate = Certificate(
        //     "1.0",
        //     sequenceNumber,
        // "SHA1",
        //  address(this),
        //         forDate(),
        //     0x1A1A1A1A2B4B2B2B3C3C3C3C41E4E4E51A1A1A1A2B4B2B2B3C3C3C3C41E4E4E5, temp,
        //     block.number, 0
        // );
        certificate.version ="1.0" ;
        certificate.serialNumber = sequenceNumber;
//        签名算法
        certificate.signature_alo ="SHA1";
        // 颁发者是智能合约地址  这里要考虑这个地址对于不同的证书来说是否一直是同一个地址
         certificate.issuer = address(this);
        // keccak 需要bytes类型 所以加一行从32转过去
        certificate.validNotAfter = forDate();
        certificate.holder = keccak256(pubKeyBytes);
        certificate.holder_alo ="SHA256";
        certificate.blockHeight = block.number;
//        证书签名值 将证书的内容进行签名然后赋值
        // certificate.signatureValue = keccak256(certificate);
        // 这里是生成证书 证书历史操作无，所以高度设置为-1
        // uint不能设置为负数 烦死了
        certificate.blockPreHeight = 0;
        // 通过时间触发 告诉外部 客户端
        // 事件被触发的时候，与事件关联的数据记录在区块链上
        // 外部客户端可以轻松检索到 ，外部也可以用JS来进行事件监听
        // emit CertAdded(certId);
//        把证书存入 设置的map映射结构里面
        certs[pubKey] = certificate;
        //        emit CertAdded(certificate.holder_id);
        return certificate;
    }

    function forDate() public view returns (uint) {
        // 获取当前时间
        uint256 timestamp = block.timestamp;
        // 设置到期时间  设置的时间设为整数吗 还是一个时间戳值 可以算下比如6个月的时间戳是多少
        // 一个月时间戳 3,600,000
        uint duration = 3600000 * 2;
        return timestamp + duration;
    }
    //   查看证书内容 函数
    // 设置一个mapping 根据序列号来查找证书内容
    // 0.8版本可以直接返回结构体数组
    // function getCertificate(bytes32 _id) public view returns (Certificate memory) {
    //     return testMap[1];
    // }
    // 这个返回的结果都是 0 是什么原因
    function getCert(bytes32 key) public view returns (Certificate memory){
        return certs[key];
    }
    // 撤销证书
    function Revoke_cert(Certificate memory cert, uint flag) public {
        // 证书到期的情况
        if (cert.validNotAfter <= uint(block.timestamp) || flag == 1) {
            cert.blockPreHeight = cert.blockHeight;
            //  撤销证书 操作发生在当前区块，但是撤销证书信息应该被打包在 下一个区块
            cert.blockHeight = block.number + 1;
        }
        // 主动申请的情况 设置一个字段 来区分是主动申请还是自动到期  自动到期的话 是自动执行   设置字段好像不行
        // 如果是主动申请 ，那就给字段赋个值
        emit CertRevoked(cert);
    }
    // 验证证书 获取到证书hash进行比对，然后再在双计数布隆过滤器当中找  用java直接调api获取证书hash

}
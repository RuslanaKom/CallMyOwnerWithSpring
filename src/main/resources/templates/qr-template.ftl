<html>
<head>
    <style>
        body {
            font-family: "Candara Light";
            font-size: 10pt;
        }
        .text-center {
            text-align: center;
        }
        .title0 {
            font-size: 30pt;
            margin-bottom: 20pt;
        }
        .titleS {
            font-size: 15pt;
        }
        .titleM {
            font-size: 25pt;
        }
        .titleL {
            font-size: 35pt;
        }
        .textS {
            font-size: 10pt;
        }
        .textM {
            font-size: 15pt;
        }
        .textL {
            font-size: 20pt;
        }
        .imageS {
            width: 80pt;
        }
        .imageM {
            width: 130pt;
        }
        .imageL {
            width: 200pt;
        }
    </style>
</head>
<body>
<div class="text-center">
    <div class="title0">${stuffname!}</div>
</div>
<div class="text-center">
    <div class="${titleStyle}">${headerphrase!}</div>
    <img class="${imageStyle}" src="${imgAsBase64}"/>
    <div class="${textStyle}">loststuff.com</div>
</div>
</body>
</html>




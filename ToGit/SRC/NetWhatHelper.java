public class NetWhatHelper {
    //то, что будем получать
    private String broadcast;
    private String netWorknumber;
    private String interval;
    private String numberOfHosts;
    //то что примем от пользователя
    private String mask;
    private String ip;

    public NetWhatHelper(String mask, String ip) {
        this.mask = mask;
        this.ip = ip;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public String getNetWorknumber() {
        return netWorknumber;
    }

    public String getInterval() {
        return interval;
    }

    public String getNumberOfHosts() {
        return numberOfHosts;
    }

    //на вход получаем строку вида 192.168.0.1
    //делаем из нее массив mass int[4], где mass[0] = 192, итд
    //это понадобится для логической операции ip & mask
    public int[] getIp(String ip)
    {
        int[] ipFormat = new int[4];
        String[] helper = ip.split("\\.");
        for (int i = 0; i < 4;i++)
        {
            String octet = helper[i];
            ipFormat[i] = Integer.parseInt(octet);
        }
        return (ipFormat);
    }

    //также на вход получаем маску вида mask = "12"
    //переводим ее в бинарный вид, где  первые 12 бит единицы, остальные (32-12)бит 0
    //возвращаем массив строк по 8 бит в каждой строке

    public String[] maskToBinaryString(String mask)
    {
        int j = 0;
        StringBuilder[] binaryMask = new StringBuilder[4];
        binaryMask[0] = new StringBuilder();
        Integer msk = Integer.parseInt(mask);
        for (int i = 0; i < 32; i++)
        {
            if (i % 8 == 0 && i != 0) {
               // System.out.println(binaryMask[j]);
                j++;
                binaryMask[j] = new StringBuilder();
            }
            if (i < msk)
                binaryMask[j].append('1');
            else
                binaryMask[j].append('0');
        }
        String res[] = new String[4];
        int i = 0;
        for (StringBuilder stringBuilder : binaryMask) {
            res[i] = stringBuilder.toString();
            i++;
        }
        return (res);
    }

    //получем номер сети путем логического "И"
    //умножаем каждый октет маски на каждый октет айпи адреса
    public String[] getNetworkNumber(int[] a, int[] b)
    {
        String[] networkNumber = new String[4];
        for (int i = 0; i < 4; i++)
        {
            networkNumber[i] = new String();
            //логическое И, числа передаем в 10-ной системе
            //логическое И под капотом
            networkNumber[i] = String.valueOf(a[i] & b[i]);
        }
        return networkNumber;
    }

    //получаем адрес широковещательного соединения
    public String[] getBinaryBroadcast(String[] networkNumber, String mask)
    {
        Integer msk = Integer.parseInt(mask);
        StringBuilder binarynetworkNumber = new StringBuilder();
        int j = 0;
        for (String s : networkNumber) {
            StringBuilder bin = new StringBuilder(Integer.toBinaryString(Integer.parseInt(s)));
            //добиваем октет до октета(8 бит), поскольку БинариСтринг вернет строку без лидирующих нулей
            while (bin.length() < 8)
                bin.insert(0,'0');
            binarynetworkNumber.append(bin);
            j++;
        }
        //берем последние (32 - размер_маски)бита и заменяем на 1
        for (int i = binarynetworkNumber.length() - 1; i >= msk; i--)
            binarynetworkNumber.setCharAt(i, '1');
        String[] broadcast = new String[4];
        j = 0;
        //нарубаем по 8 бит(октеты), чтобы далее осуществить перевод в читаемый(десятичный вид)
        for(int i = 0; i < 32;)
        {
            broadcast[j] = binarynetworkNumber.substring(i, (i + 8));
            i += 8;
            j++;
        }
        return broadcast;
    }

    //перевод массива бинарных строк в 10-чную систему
    public String[] binaryToString(String s[])
    {
        String[] octets = new String[4];
        int i = 0;
        for (String str : s)
        {
            octets[i] = String.valueOf(Integer.parseInt(str, 2));
            i++;
        }
        return octets;
    }

    //перевод массива бинарных строк в 10-чную систему
    public int[] binaryToInt(String s[])
    {
        int[] octets = new int[4];
        int i = 0;
        for (String str : s) {
            octets[i] = Integer.parseInt(str, 2);
            i++;
        }
        return (octets);
    }

    //нахождение числа возможных хостов
    public void findNumberOfHosts(String mask)
    {
        Integer msk = Integer.parseInt(mask);
        //число хостов равно 2 в степени (32 - маска) - 2
        //-2, поскольку 1 уходит на адрес сети, 1 на широковещательное соединение
        long res = (long) (Math.pow(2, (32 - msk)) - 2);
        numberOfHosts = String.valueOf(res);
    }


    //получение интервала доступных хостов
    public void getInterval(String[] netWorkAdress, String[] broadcast)
    {
        interval = new String();
        //последний хост-тот, что предшествует адресу broadcast
        String lastOctmax = String.valueOf(Integer.parseInt(broadcast[3]) - 1);
        //первый хост-следующий за адресом сети
        String minOctMin = String.valueOf(Integer.parseInt(netWorkAdress[3]) + 1);
        //Формируем результирующую строку
        for (int i = 0; i < 3; i++)
        {
            interval += netWorkAdress[i];
            interval += '.';
        }
        interval += minOctMin + '-';
        for (int i = 0; i < 3; i++)
        {
            interval += broadcast[i];
            interval += '.';
        }
        interval += lastOctmax;
    }

    public String getStringFromOctets(String[] networkAdress)
    {
        String networkStr = new String();
        for(int i = 0; i < 4; i++) {
            networkStr += networkAdress[i];
            networkStr += '.';
        }
        return networkStr.substring(0, networkStr.length() - 1);
    }

    public void run() {
        int[] ipAdress = getIp(ip);
        String[] maskInBin = maskToBinaryString(mask);
        int[] maskInInt = binaryToInt(maskInBin);
        String[] netWorkOct = getNetworkNumber(ipAdress, maskInInt);
        netWorknumber = getStringFromOctets(netWorkOct);
        String[] broadcastInBin = getBinaryBroadcast(netWorkOct, mask);
        String[] broadcastInInt = binaryToString(broadcastInBin);
        broadcast = getStringFromOctets(broadcastInInt);
        getInterval(netWorkOct, broadcastInInt);
        findNumberOfHosts(mask);
    }
}

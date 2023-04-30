import java.util.*;

public class Test {
    private static int limit;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Test test = new Test();
        String function;

        do
        {
            System.out.println("\nIncrement = testing with repeated random order and function with increasing number of variables");
            System.out.println("Best = create best order with comparison testing");
            System.out.println("Same = testing with repeated random order and function");
            System.out.print("End = end of testing -> ");

            function = scanner.nextLine();
            switch (function.toLowerCase()) {
                case "increment" -> test.incrementTesting(scanner);
                case "best" -> test.bestOrderTesting(scanner);
                case "same" -> test.sameTesting(scanner);
                case "end" -> {}
                default -> System.out.println("Wrong input");
            }
        }
        while(!function.equals("end"));
    }
    private BDD generatingData(int limit) {
        long startTime,endTime;
        double reduction,time;
        BDD robdd;

        startTime =  System.nanoTime();
        robdd = create(bfunctionGen(limit),orderGen(limit));
        endTime = System.nanoTime();

        reduction = 100 - (robdd.getNumberOfNodes() / (Math.pow(2, limit)-1));
        time = (double)(endTime-startTime)/1000000000;

        System.out.println("Duration of 2^"+(limit +1)+"-1 original nodes, BDD reduced into "+robdd.getNumberOfNodes() + " unique nodes with approximately "+ reduction+" % reduction, duration of creating: "+ time + " seconds.");
        return robdd;
    }
    public void sameTesting(Scanner scanner) {
        System.out.print("\nChoose lenght of performing same testing -> ");
        int precission = Integer.parseInt(scanner.nextLine());

        System.out.print("\nChoose number of variables -> ");
        setLimit(Integer.parseInt(scanner.nextLine()));
        BDD robdd = null;

        for(int i = 0; i < precission; i++)
        {
            robdd = generatingData(limit);
        }

        assert robdd != null;
        useTesting(scanner,robdd);
    }
    public void incrementTesting(Scanner scanner){
        System.out.print("\nChoose maximum of varibles -> ");
        int maxVariable = Integer.parseInt(scanner.nextLine());
        BDD robdd = null;

        for(int i = 1; i <= maxVariable; i++)
        {
            robdd = generatingData(i);
            setLimit(i);
        }

        assert robdd != null;
        useTesting(scanner,robdd);
    }
    public void bestOrderTesting(Scanner scanner){
        System.out.print("Choose number of variables -> ");
        setLimit(Integer.parseInt(scanner.nextLine()));

        String bfunction = bfunctionGen(limit);
        BDD optimalizedBDD = createWithBestOrder(bfunction);
        BDD randomBDD = create(bfunction,orderGen(limit));

        double reduction1 = 100 - (optimalizedBDD.getNumberOfNodes() / (Math.pow(2,limit)-1));
        double reduction2 = 100 - (randomBDD.getNumberOfNodes() / (Math.pow(2,limit)-1));

        System.out.println("\nOptimalized BDD has "+optimalizedBDD.getNumberOfNodes()+ " unique nodes with order "+optimalizedBDD.getOrder() +", "+reduction1+ " % reduction");
        System.out.println("Random BDD has "+randomBDD.getNumberOfNodes() + " unique nodes with order "+randomBDD.getOrder() +", "+reduction2+" % reduction");
        System.out.println((randomBDD.getNumberOfNodes() - optimalizedBDD.getNumberOfNodes()) +" unique node difference " +(reduction1 - reduction2) +" % reduction difference");

    }
    public void useTesting(Scanner scanner,BDD robdd) {
        System.out.print("\nWould you like to test use on your last BDD? -> ");
        String decission, order = robdd.getOrder();

        do {
            decission = scanner.nextLine();
            switch (decission.toLowerCase()) {
                case "yes" -> {
                    String input,BDDoutput,expressionOutput;
                    int failed = 0;

                    System.out.println("Order: "+order);
                    for(int i = 0; i < Math.pow(2,limit);i++)
                    {
                        input = inputGen(i);
                        BDDoutput = robdd.use(input);
                        expressionOutput  = finalExpression(input,robdd);

                        System.out.print("Input " + input + " expected: " + expressionOutput +" BDD result: "+BDDoutput);
                        if(BDDoutput.equals(expressionOutput))
                            System.out.println(" ✅");
                        else
                        {
                            System.out.println(" ❌");
                            failed ++;
                        }
                    }
                    System.out.println("Succesfull: "+(long)(Math.pow(2,limit)-failed)+" Failed: "+failed);
                }
                case "no" -> {}
                default -> System.out.print("\nWould you like to test use on your last BDD? -> ");
            }
        }
        while(!decission.equals("no") && !decission.equals("yes"));
    }
    public String finalExpression(String input,BDD robdd) {
        String temp = robdd.getRoot().getBfuction();
        String[] splitedTemp = temp.split("[+]",0);
        boolean zero = false;

        for(String term: splitedTemp)
        {
            for(int i = 0; i < term.length(); i++)
            {
                zero = false;
                if(i == 0)
                {
                    if(term.charAt(i) != '!' && input.charAt(robdd.getOrder().indexOf(String.valueOf(term.charAt(i)))) == '0')
                    {
                        zero = true;
                        break;
                    }
                }
                else if(term.charAt(i-1) == '!')
                {
                    if(input.charAt(robdd.getOrder().indexOf(String.valueOf(term.charAt(i)))) == '1')
                    {
                        zero = true;
                        break;
                    }
                }
                else if(term.charAt(i) != '!')
                {
                    if(input.charAt(robdd.getOrder().indexOf(String.valueOf(term.charAt(i)))) == '0')
                    {
                        zero = true;
                        break;
                    }
                }
            }
            if(!zero)
                return "1";
        }
        return "0";
    }
    public BDD create(String bfunction, String order) {
        BDD bdd = new BDD(bfunction,order);
        HashMap<String,Node> hashTable = new HashMap<>();

        hashTable.put(bdd.getRoot().getBfuction(),bdd.getRoot());
        bdd.setRoot(nodeRecursion(bdd.getRoot(),order,0,hashTable));

        hashTable.clear();
        hashTable.put(bdd.getRoot().getBfuction(),bdd.getRoot());

        nodeCounting(bdd.getRoot(),hashTable);
        bdd.setNumberOfNodes(hashTable.size());
        bdd.setOrder(order);

        return bdd;
    }
    private void nodeCounting(Node root,HashMap<String,Node> hashTable) {
        if(root == null)
        {
            return;
        }
        nodeCounting(root.getLeftchild(),hashTable);
        hashTable.putIfAbsent(root.getBfuction(),root);
        nodeCounting(root.getRightchild(),hashTable);

    }
    private Node nodeRecursion(Node root,String order,int i,HashMap<String,Node> hashTable){
        Node newNode;

        if(root.getLeftchild() == null && i < order.length()  && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")))
        {
            while(!root.getBfuction().contains(String.valueOf(order.charAt(i))))
                i++;

            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'N')));
            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setLeftchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        if(root.getRightchild() == null && i < order.length()  && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")))
        {
            while(!root.getBfuction().contains(String.valueOf(order.charAt(i))))
                i++;

            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'P')));
            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setRightchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        if(root.getRightchild() == root.getLeftchild() && root.getRightchild() != null && root.getLeftchild() != null)
        {
            root = root.getRightchild();
        }

        return root;
    }
    public BDD createWithBestOrder(String bfunction) {
        BDD minimum = null, iteration;

        for(int i = 0; i < limit; i++)
        {
            iteration = create(bfunction,orderGen(limit));

            if(minimum == null || minimum.getNumberOfNodes() > iteration.getNumberOfNodes())
            {
                minimum = iteration;
            }
        }
        return minimum;
    }
    private String decomposition(String bfunction, char order,char choice) {
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(bfunction.split("[+]",0)));
        ArrayList<String> finalList = new ArrayList<>(functionList);

        for(String bool: functionList)
        {
            switch (choice) {
                case 'N' -> {
                    if (bool.contains("!" + order))
                    {
                        finalList.set(finalList.indexOf(bool), bool.replace("!" + order, ""));
                        if (bool.replace("!" + order, "").equals(""))
                            return "1";
                    }
                    else if (bool.contains(String.valueOf(order)))
                    {
                        finalList.remove(bool);
                        if (finalList.isEmpty())
                            return "0";
                    }
                }
                case 'P' -> {
                    if (bool.contains("!" + order))
                    {
                        finalList.remove(bool);
                        if (finalList.isEmpty())
                            return "0";
                    }
                    else if (bool.contains(String.valueOf(order)))
                    {
                        finalList.set(finalList.indexOf(bool), bool.replace(String.valueOf(order), ""));
                        if (bool.replace(String.valueOf(order), "").equals(""))
                            return "1";
                    }
                }
            }
        }
        return booleanSimplifing(String.join("+",finalList));
    }
    private String bfunctionGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        ArrayList<Character> list2 = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        //int max = random.nextInt(1,(int)Math.pow(2,limit));

        for(int i = 0; i < limit; i++)
        {
            if(i != 0)
            {
                list.add('+');
                list2.clear();
            }

            for(int j = 0; j < limit; j++)
            {

                list2.add((char)(65 + j));
            }

            int max2 = (int)(limit*random.nextGaussian(0,0.20));
            max2 = max2 < 0 ? -max2 : max2;

            for(int j = 0; j < max2; j++)
            {
                int max3 = random.nextInt(0,limit-j);
                list2.remove(max3);
            }

            for(char character: list2)
            {
                if(random.nextInt(2) == 0)
                    list.add('!');

                list.add(character);
            }

        }

        for (Character ch : list)
            builder.append(ch);

        return booleanSimplifing(builder.toString());
    }
    private String orderGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < limit; i++)
        {
            list.add((char)(65 + i));
        }
        Collections.shuffle(list,new Random());

        for (Character ch : list)
            builder.append(ch);

        return builder.toString();
    }
    private String inputGen(int number) {
        String result = Integer.toBinaryString(number);
        if(result.length() < limit)
        {
            result = "0".repeat(limit - result.length()) + result;
        }

        return result;
    }
    private String booleanSimplifing(String bfunction){
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(bfunction.split("[+]",0)));

        for(int i = 0; i < functionList.size(); i++){
            for(int j = i + 1; j < functionList.size(); j++){
                if(functionList.get(i).equals(functionList.get(j)) || functionList.get(j).contains("0") ){      //A + A || A + 0 -> A
                    functionList.remove(j);
                    break;
                }
            }
        }
        return String.join("+",functionList);
    }
    private static void setLimit(int limit) {
        Test.limit = limit;
    }
}
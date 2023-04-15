package vitaliiev.portst;

public class PortServiceConsumer {
    public static void main(String[] args) {
        String[] index = new String[] {"1,3-5", "2", "3-4"};
        Port port = new DefaultPort();
        port.uniqueGroups(index);
        System.out.println(port);
    }
}

public class Carrot {

    public static void printStart() {
        String logo = """
         ____                     _     
        / ___| __ _ _ __ _ __ ___| |_   
       | |    / _` | '__| '__/ _ \\ __|  
       | |___| (_| | |  | | | (_) | |_  
        \\____|\\__,_|_|  |_|  \\___/ \\__| 
       """;
        System.out.println(logo);
        System.out.println("Hello! I'm C4RR0T\nWhat can I do for you?");
        System.out.println("------------------------------------------");
    }

    public static void printExit() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("------------------------------------------");
    }

    public static void main(String[] args) {
        printStart();
        printExit();
    }
}

public class MyArrayList {

    //private String[] array = new String[10];
    // Creates a new object array :)
    private UserCredentials[] array2 = new UserCredentials[10];
    private int next = 0;

    public UserCredentials get(int index) {
        return array2[index];
    }

    /**
     * Adds the given String into the array
     * @param user - the value you want to add into the array
     * @return - index where the String was added
     */

    //Add new String to Array
    public int add(UserCredentials user) {

        if(next >= array2.length) {
            resize();
        }

        array2[next] = user;
        return next++;
    }

    //Add String at this Location
    public int add(UserCredentials user, int index) {

        if(next <= 0 || index > next) {
            return add(user);
        }

        if(next >= array2.length) {
            resize();
        }
        //We are going to have to make space for the new String at the index
        //by moving all numbers at that index onward.
        //We also need to make sure that the index provided is within the current size.

        //'i' will represent the index of the String that is going to move.
        for(int i = (next - 1); i >= index; i--) {
            array2[i + 1] = array2[i];
        }
        array2[index] = user;
        next++;

        return index;
    }

    //Expands or contracts MyArrayList
    public void resize() {
        UserCredentials[] newArray = new UserCredentials[array2.length * 2];
        for(int i = 0; i < array2.length; i++) {
            newArray[i] = array2[i];
        }

        this.array2 = newArray;
    }

    //Returns length
    public int size() {
        return next;
    }

    @Override
    public String toString() {
        String result = "[";

        for(int i = 0; i < size(); i++) {
            result += array2[i];
            if(i < size() - 1) result += ", ";
        }
        result += "]";

        return result;
    }

    //isEmpty()
    public boolean isEmpty() {
        return size() == 0;
    }

    //remove

    //replace

    //add/remove -> some index number

}

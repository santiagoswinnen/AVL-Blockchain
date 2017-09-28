public class DataPair<S,T> {
    S element1;
    T element2;
    public DataPair(S element1, T element2){
        this.element1 = element1;
        this.element2 = element2;
    }
    public S getElement1() {
        return element1;
    }

    public T getElement2() {
        return element2;
    }
}

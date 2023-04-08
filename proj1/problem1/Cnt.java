class Cnt { //class to hold sum of prime #
    private int cnt = 0;
    public void inc() {
        this.cnt++;
    }
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    public int getCnt() {
        return cnt;
    }
    public static boolean isPrime(int x) {
        if (x <= 1) return false;
        for (int i = 2; i <= (int)Math.sqrt(x); i++) { // integers that exceed sqrt(x) should have its pair below sqrt(x)
            if (x % i == 0) return false;
        }
        return true;
    }
}
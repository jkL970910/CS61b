//Make sure to make this class a part of the synthesizer package
package synthesizer;

//import edu.princeton.cs.algs4.StdAudio;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer<>((int) Math.round(SR / frequency));
        for (int i = 0; i < buffer.capacity(); i += 1) {
            buffer.enqueue(0.0);
        }
        //Create a buffer with capacity = SR / frequency. You'll need to
        //cast the result of this divsion operation into an int. For better
        //accuracy, use the Math.round() function before casting.
        //Your buffer should be initially filled with zeros.
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for (int i = 0; i < buffer.capacity(); i += 1) {
            buffer.dequeue();
            double r = Math.random() - 0.5;
            buffer.enqueue(r);
        }
        //Dequeue everything in the buffer, and replace it with random numbers
        //between -0.5 and 0.5. You can get such a number by using:
        //double r = Math.random() - 0.5;
        //
        //Make sure that your random numbers are different from each other.
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double toMove = buffer.dequeue();
        //StdAudio.play(toMove);
        double averageNum = (toMove + buffer.peek()) * 0.5 * DECAY;
        buffer.enqueue(averageNum);

        //Dequeue the front sample and enqueue a new sample that is
        //the average of the two multiplied by the DECAY factor.
        //Do not call StdAudio.play().
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        //Return the correct thing.
        return buffer.peek();
    }
}

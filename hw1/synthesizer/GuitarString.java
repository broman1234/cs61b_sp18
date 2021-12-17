// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import synthesizer.AbstractBoundedQueue;

import java.util.ArrayList;
import java.util.List;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = 0.996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this divsion operation into an int. For better
        //       accuracy, use the Math.round() function before casting.
        //       Your buffer should be initially filled with zeros.
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);

        for (int i = 0; i < buffer.capacity(); i += 1) {
            buffer.enqueue(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // TODO: Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each other.
        for (int i = 0; i < buffer.capacity(); i += 1) {
            buffer.dequeue();
            double r = Math.random() - 0.5;
            int k = 0;
            for (double s : buffer) {
                if (r == s) {
                    break;
                }
                k += 1;
            }
            if (k == buffer.fillCount()) {
                buffer.enqueue(r);
            } else {
                i -= 1;
            }
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().
        double removeItem = buffer.dequeue();
        double firstItem = buffer.peek();
        double newItem = (removeItem + firstItem) / 2 * DECAY;
        buffer.enqueue(newItem);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // TODO: Return the correct thing.
        return buffer.peek();
    }

    @Override
    public String toString() {
        StringBuilder returnSB = new StringBuilder("{");
        for (double x : buffer) {
            returnSB.append(x);
            returnSB.append(", ");
        }
        returnSB.append("}");
        return returnSB.toString();
    }
}

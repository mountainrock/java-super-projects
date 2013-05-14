package utils.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.Transformer;


/**
 * A Transformer that applies a given set of transformations on
 * the given input
 * in the proscribed order; much like a Unix pipe-line. Each filter in
 * the chain provides input to the next filter in the chain.
 *
 * <p>Use TransformerChain whenever you can meet your complex
 * data conversion by pipe-lining several simpler conversions together.
 *
 */
public class TransformerChain implements Transformer
{
  private Collection _transformers = new ArrayList();

  /**
   * Add the given transformer to the chain.
   * @param t The Transformer to be added to the end of the chain.
   */
  public void addTransformer(Transformer t) {
    if (t == null) {
      throw new IllegalArgumentException("Null Transformer is not allowed");
    }
    _transformers.add(t);
  }

  /**
   * Transform the given object by applying each of the transformers in
   * the chain starting with the given subject, and feeding the output of
   * one transformer to the input of the next.
   * @param subject  the object to be transformed, possibly null.
   * @return the transformed result, possibly null.
   */
  public Object transform(Object subject)
  {

    for (Iterator iterator = _transformers.iterator(); iterator.hasNext();) {
      Transformer transformer = (Transformer) iterator.next();
      subject = transformer.transform(subject);
    }
    return subject;
  }

}

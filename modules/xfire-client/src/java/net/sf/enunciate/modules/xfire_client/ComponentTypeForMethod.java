package net.sf.enunciate.modules.xfire_client;

import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.declaration.TypeDeclaration;
import freemarker.template.TemplateModelException;
import net.sf.jelly.apt.decorations.type.DecoratedTypeMirror;
import net.sf.jelly.apt.decorations.TypeMirrorDecorator;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

/**
 * Converts a fully-qualified class name to its alternate client fully-qualified class name.
 *
 * @author Ryan Heaton
 */
public class ComponentTypeForMethod extends ClientClassnameForMethod {

  public ComponentTypeForMethod(Map<String, String> conversions) {
    super(conversions);
  }

  public ComponentTypeForMethod(Map<String, String> conversions, boolean jdk15) {
    super(conversions, jdk15);
  }

  @Override
  protected String convert(TypeMirror typeMirror) throws TemplateModelException {
    if (typeMirror instanceof ArrayType) {
      return super.convert(((ArrayType) typeMirror).getComponentType());
    }
    else if (typeMirror instanceof DeclaredType) {
      DecoratedTypeMirror decoratedTypeMirror = (DecoratedTypeMirror) TypeMirrorDecorator.decorate(typeMirror);
      if (decoratedTypeMirror.isCollection()) {
        DeclaredType declaredType = (DeclaredType) typeMirror;
        Iterator<TypeMirror> actualTypeArguments = declaredType.getActualTypeArguments().iterator();
        if (actualTypeArguments.hasNext()) {
          return super.convert(actualTypeArguments.next());
        }
      }
    }

    throw new TemplateModelException("No component type for " + typeMirror);
  }

}

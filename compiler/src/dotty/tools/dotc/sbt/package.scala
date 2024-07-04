package dotty.tools.dotc.sbt

import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.Symbol
import dotty.tools.dotc.core.NameOps.stripModuleClassSuffix
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Names.termName

inline val TermNameHash = 1987 // 300th prime
inline val TypeNameHash = 1993 // 301st prime
inline val InlineParamHash = 1997 // 302nd prime

extension (sym: Symbol)

  /** Mangle a JVM symbol name in a format better suited for internal uses by sbt.
   *  WARNING: output must not be written to TASTy, as it is not a valid TASTy name.
   */
  private[sbt] def zincMangledName(using Context): Name =
    if sym.isConstructor then
      // TODO: ideally we should avoid unnecessarily caching these Zinc specific
      // names in the global chars array. But we would need to restructure
      // ExtractDependencies caches to avoid expensive `toString` on
      // each member reference.
      termName(sym.owner.fullName.mangledString.replace(".", ";").nn ++ ";init;")
    else
      sym.name.stripModuleClassSuffix

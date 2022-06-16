package di;

/**
 * 循环依赖异常
 *
 * @author leiyutian yutianlei@shein.com
 * @version V1.0
 * @date 2022/5/17 13:36
 */
public class CycleDependenciesFoundException extends RuntimeException {
    public Class<?>[] getComponents() {
        return null;
    }
}

